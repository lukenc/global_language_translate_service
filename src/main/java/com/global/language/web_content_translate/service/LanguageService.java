package com.global.language.web_content_translate.service;

import com.global.language.web_content_translate.model.bo.LanguageBo;
import com.global.language.web_content_translate.model.enity.Language;

import java.util.List;

public interface LanguageService {
    int add(String name,String isoCode,String nativeName,String userId);

    int edit(Integer id,String name,String isoCode,String nativeName,String userId);

    int delete(Integer id,String userId);

    List<LanguageBo> selectAllLanguageList();

    Language getLanguageByIso(String iso);
}
