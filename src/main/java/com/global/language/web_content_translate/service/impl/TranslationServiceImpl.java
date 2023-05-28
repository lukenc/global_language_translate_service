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

    @Override
    public List<TranslationBo> getTranslationByContentId(Integer contentId) {
        List<Translation> translationList=  translationMapper.getTranslationByContentId(contentId);
        return attachContentInfo(translationList);
    }

    @Override
    public Map<Integer, Integer> getContentTranslatedCount(List<Integer> contentIdList) {
        List<IdCount> idCountList = translationMapper.getContentTranslatedCount(contentIdList);
        return idCountList != null ? idCountList.stream().collect(Collectors.toUnmodifiableMap(IdCount::getId, IdCount::getCount)) : Map.of();
    }

    private List<TranslationBo> attachContentInfo( List<Translation> translationList){
        if (translationList==null){
            return Collections.emptyList();
        }
        List<Integer> contentIdList = translationList.stream().map(Translation::getContentId).toList();
        if (!contentIdList.isEmpty()){
            List<WebContentBo> webContentList = webContentService.getWebContentListByIdList(contentIdList);
            if (webContentList!=null){
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





    private int createTranslationFromExcel(Integer languageId,String languageCode,Integer contentId,String translatedText,String userId) {
           Translation ori = translationMapper.getTranslationByContentIdAndLanguage(contentId, languageId);
           if (ori!=null){
              return edit(ori.getId(),languageCode,languageId,contentId,translatedText,userId);
           }else {
              return add(languageCode,languageId,contentId,translatedText,userId);
           }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }
    private Integer getIntValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return (int) cell.getNumericCellValue();
    }
}
