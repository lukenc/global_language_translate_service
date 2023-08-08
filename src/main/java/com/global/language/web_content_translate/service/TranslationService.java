package com.global.language.web_content_translate.service;

import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.bo.TranslationBo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 翻译服务接口
 */
public interface TranslationService {

    /**
     * 添加翻译
     *
     * @param languageCode 语言代码
     * @param languageId   语言ID
     * @param contentId    内容ID
     * @param translatedText 翻译后的文本
     * @param userId       用户ID
     * @return 添加的翻译数量
     */
    int add(String languageCode, Integer languageId, Integer contentId, String translatedText, String userId);

    /**
     * 编辑翻译
     *
     * @param id            翻译ID
     * @param languageCode  语言代码
     * @param languageId    语言ID
     * @param contentId     内容ID
     * @param translatedText 翻译后的文本
     * @param userId        用户ID
     * @return 编辑的翻译数量
     */
    int edit(Integer id, String languageCode, Integer languageId, Integer contentId, String translatedText, String userId);

    /**
     * 删除翻译
     *
     * @param id     翻译ID
     * @param userId 用户ID
     * @return 删除的翻译数量
     */
    int delete(Integer id, String userId);

    /**
     * 根据语言ID获取翻译列表
     *
     * @param languageId 语言ID
     * @return 翻译列表
     */
    List<TranslationBo> getTranslationByLanguageId(Integer languageId);

    /**
     * 根据语言代码获取翻译列表
     *
     * @param languageCode 语言代码
     * @return 翻译列表
     */
    List<TranslationBo> getTranslationByLanguageCode(String languageCode);

    /**
     * 根据内容ID获取翻译列表
     *
     * @param contentId 内容ID
     * @return 翻译列表
     */
    List<TranslationBo> getTranslationByContentId(Integer contentId);

    /**
     * 获取内容翻译数量
     *
     * @param contentIdList 内容ID列表
     * @return 内容ID与翻译数量的映射
     */
    Map<Integer, Integer> getContentTranslatedCount(List<Integer> contentIdList);

    /**
     * 导入翻译
     *
     * @param file   导入的文件
     * @param userId 用户ID
     * @return 操作结果
     */
    OperationResult<?> importTranslations(MultipartFile file, String userId);
}
