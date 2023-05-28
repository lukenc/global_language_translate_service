package com.global.language.web_content_translate.service;

import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.bo.TranslationBo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TranslationService {
    int add(String languageCode,Integer languageId,Integer contentId,String translatedText,String userId);

    int edit(Integer id, String languageCode, Integer languageId, Integer contentId, String translatedText, String userId);

    int delete(Integer id, String userId);


    List<TranslationBo> getTranslationByLanguageId(Integer languageId);

    List<TranslationBo> getTranslationByLanguageCode(String languageCode);

    List<TranslationBo> getTranslationByContentId(Integer contentId);

    Map<Integer,Integer> getContentTranslatedCount(List<Integer> contentIdList);

    OperationResult<?> importTranslations(MultipartFile file, String userId);
}
