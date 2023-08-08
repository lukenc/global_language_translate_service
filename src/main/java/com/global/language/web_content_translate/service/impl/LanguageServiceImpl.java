package com.global.language.web_content_translate.service.impl;

import com.global.language.web_content_translate.model.bo.LanguageBo;
import com.global.language.web_content_translate.model.enity.Language;
import com.global.language.web_content_translate.repository.LanguageMapper;
import com.global.language.web_content_translate.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author zz
 */
@Service
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    LanguageMapper languageMapper;

    @Override
    public int add(String name, String isoCode, String nativeName, String userId) {

        Language language= Language.create(name,isoCode,nativeName);
        language.setCreatedBy(userId);
        language.setCreatedAt(new Date(Instant.now().toEpochMilli()));
        return languageMapper.insertSelective(language);
    }

    @Override
    public int edit(Integer id, String name, String isoCode, String nativeName, String userId) {
        Language language= Language.create(name,isoCode,nativeName);
        language.setId(id);
        language.setModifiedBy(userId);
        language.setModifiedAt(new Date(Instant.now().toEpochMilli()));
        return languageMapper.updateByPrimaryKeySelective(language);
    }

    @Override
    public int delete(Integer id,String userId) {

        return languageMapper.delete(id,userId);
    }


/**
 * 获取所有语言列表
 * @return 语言业务对象列表
 */
@Override
public List<LanguageBo> selectAllLanguageList() {
    // 获取所有语言列表
    List<Language> languageList = languageMapper.getAllLanguageList();

    // 如果语言列表不为空
    if (languageList != null) {
        // 将语言列表转换为语言业务对象列表并返回
        return languageList.stream().map(LanguageBo::new).toList();
    }

    // 如果语言列表为空，则返回空集合
    return Collections.emptyList();
}


    @Override
    public Language getLanguageByIso(String iso) {

        return languageMapper.selectByIso(iso);
    }
}
