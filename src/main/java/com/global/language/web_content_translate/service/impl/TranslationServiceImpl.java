package com.global.language.web_content_translate.service.impl;

import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.bo.TranslationBo;
import com.global.language.web_content_translate.model.bo.WebContentBo;
import com.global.language.web_content_translate.model.enity.IdCount;
import com.global.language.web_content_translate.model.enity.Language;
import com.global.language.web_content_translate.model.enity.Translation;
import com.global.language.web_content_translate.repository.TranslationMapper;
import com.global.language.web_content_translate.service.LanguageService;
import com.global.language.web_content_translate.service.TranslationService;
import com.global.language.web_content_translate.service.WebContentService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TranslationServiceImpl implements TranslationService {
    @Autowired
    TranslationMapper translationMapper;
    @Autowired
    WebContentService webContentService;
    @Autowired
    LanguageService languageService;

    @Override
    public int add(String languageCode, Integer languageId, Integer contentId, String translatedText,String userId) {
        Translation translation = Translation.create(languageCode,languageId,contentId,translatedText);
        translation.setCreatedAt(new Date(Instant.now().toEpochMilli()));
        translation.setCreatedBy(userId);
        return translationMapper.insertSelective(translation);
    }


    @Override
    public int edit(Integer id, String languageCode, Integer languageId, Integer contentId, String translatedText, String userId) {
        Translation translation = Translation.create(languageCode,languageId,contentId,translatedText);
        translation.setId(id);
        translation.setModifiedAt(new Date(Instant.now().toEpochMilli()));
        translation.setModifiedBy(userId);
        return translationMapper.updateByPrimaryKeySelective(translation);
    }

    @Override
    public int delete(Integer id, String userId) {
        Translation translation = new Translation();
        translation.setId(id);
        translation.setModifiedAt(new Date(Instant.now().toEpochMilli()));
        translation.setModifiedBy(userId);
        translation.setEnable(false);
        return translationMapper.updateByPrimaryKeySelective(translation);
    }

    @Override
    public List<TranslationBo> getTranslationByLanguageId(Integer languageId) {
      List<Translation> translationList=  translationMapper.getTranslationByLanguageId(languageId);
        return attachContentInfo(translationList);
    }

    @Override
    public List<TranslationBo> getTranslationByLanguageCode(String languageCode) {
        List<Translation> translationList=  translationMapper.getTranslationByLanguageCode(languageCode);
        return attachContentInfo(translationList);
    }


/**
 * 根据内容ID获取翻译列表
 * @param contentId 内容ID
 * @return 翻译业务对象列表
 */
@Override
public List<TranslationBo> getTranslationByContentId(Integer contentId) {
    // 调用数据访问层，根据内容ID获取翻译列表
    List<Translation> translationList = translationMapper.getTranslationByContentId(contentId);

    // 调用附加内容信息的方法，将翻译列表与内容信息关联起来
    return attachContentInfo(translationList);
}



/**
 * 获取内容翻译计数的方法
 * @param contentIdList 内容ID列表
 * @return 内容ID和对应翻译计数的映射关系
 */
@Override
public Map<Integer, Integer> getContentTranslatedCount(List<Integer> contentIdList) {
    // 调用translationMapper的方法获取内容翻译计数列表
    List<IdCount> idCountList = translationMapper.getContentTranslatedCount(contentIdList);

    // 如果idCountList不为空，则将其转换为不可修改的映射关系并返回，否则返回一个空的映射关系
    return idCountList != null ? idCountList.stream().collect(Collectors.toUnmodifiableMap(IdCount::getId, IdCount::getCount)) : Map.of();
}


/**
 * 附加内容信息到翻译列表中
 * @param translationList 翻译列表
 * @return 附加了内容信息的翻译业务对象列表
 */
private List<TranslationBo> attachContentInfo(List<Translation> translationList) {
    if (translationList == null) {
        return List.of();
    }

    List<Integer> contentIdList = translationList.stream().map(Translation::getContentId).toList();
    if (!contentIdList.isEmpty()) {
        List<WebContentBo> webContentList = webContentService.getWebContentListByIdList(contentIdList);
        if (webContentList != null) {
            Map<Integer, WebContentBo> id2WebContentMap = webContentList.stream().collect(Collectors.toMap(WebContentBo::getId, Function.identity()));

            // 匹配数据
            return translationList.stream().map(translation -> {
                WebContentBo webContentBo = id2WebContentMap.get(translation.getContentId());
                return TranslationBo.fromCombine(translation, webContentBo);
            }).toList();
        }
    }

    return List.of();
}


    @Override
    public OperationResult<?> importTranslations(MultipartFile file, String userId) {
        var resultString = new StringJoiner(System.lineSeparator());
        var successNum=0;
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // 跳过第一行表头
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            if (file.getOriginalFilename()==null){
                OperationResult.fail("文件名称不合法");
            }
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileName=fileName.split("\\.")[0];
            String iso=fileName;
            Language language= languageService.getLanguageByIso(iso);
            if (language==null){
                return OperationResult.fail("不存在的语种翻译。");
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // 解析每一列的数据
                Integer contentId = getIntValue(row.getCell(0));
                String url = getStringValue(row.getCell(1));
                String content = getStringValue(row.getCell(2));
                String translationText = getStringValue(row.getCell(4));
                if (!StringUtils.hasText(translationText)){
                    resultString.add("""
                    url: %s
                    内容:%s
                    处理结果: %s
                    消息: %s
                    %n
                    """.formatted(url,content, "失败", "数据库中不存在该内容，未填写翻译内容"));
                    continue;
                }

                WebContentBo webContentBo= webContentService.getWebContentById(contentId);
                if (webContentBo!=null){
                  int res= createTranslationFromExcel(language.getId(),language.getIsoCode(),webContentBo.getId(),translationText,userId);
                  if (res>0){
                      successNum++;
                  }
                }else {
                    resultString.add("""
                    url: %s
                    内容:%s
                    处理结果: %s
                    消息: %s
                    %n
                    """.formatted(url,content, "失败", "数据库中不存在该内容，尚不可以翻译"));
                }
            }
            return OperationResult.ok("成功："+successNum+resultString.toString());
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        return OperationResult.fail();
    }






/**
 * 从Excel中创建翻译
 * @param languageId 语言ID
 * @param languageCode 语言代码
 * @param contentId 内容ID
 * @param translatedText 翻译文本
 * @param userId 用户ID
 * @return 返回翻译结果
 */
private int createTranslationFromExcel(Integer languageId, String languageCode, Integer contentId, String translatedText, String userId) {
    // 根据内容ID和语言ID获取原始翻译
    Translation ori = translationMapper.getTranslationByContentIdAndLanguage(contentId, languageId);
    if (ori != null) {
        // 如果原始翻译存在，则进行编辑操作
        return edit(ori.getId(), languageCode, languageId, contentId, translatedText, userId);
    } else {
        // 如果原始翻译不存在，则进行添加操作
        return add(languageCode, languageId, contentId, translatedText, userId);
    }
}



/**
 * 获取单元格的字符串值
 *
 * @param cell 单元格对象
 * @return 单元格的字符串值，如果单元格为空则返回null
 */
private String getStringValue(Cell cell) {
    if (cell == null) {
        return null;
    }

    // 根据单元格类型进行不同的处理
    return switch (cell.getCellType()) {
        case STRING -> cell.getStringCellValue(); // 如果是字符串类型，直接获取字符串值
        case NUMERIC -> String.valueOf(cell.getNumericCellValue()); // 如果是数字类型，将数字转换为字符串
        case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()); // 如果是布尔类型，将布尔值转换为字符串
        default -> null; // 其他类型返回null
    };
}


/**
 * 获取单元格的整数值。
 *
 * @param cell 单元格对象
 * @return 单元格的整数值，如果单元格为空则返回null
 */
private Integer getIntValue(Cell cell) {
    if (cell == null) {
        return null;
    }
    return (int) cell.getNumericCellValue();
}
}
