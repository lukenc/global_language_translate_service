package com.global.language.web_content_translate.repository;


import com.global.language.web_content_translate.model.enity.IdCount;
import com.global.language.web_content_translate.model.enity.Translation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TranslationMapper {
    /**
     * 
     * 删除 translation记录
     * 
     * @param id java.lang.Integer
     * @return int
     *
     * @author lukenc
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 
     * 新建 translation记录
     * 
     * @param row com.mybatis.gen_entity.Do.Translation
     * @return int
     *
     * @author lukenc
     */
    int insert(Translation row);

    /**
     * 
     * 新建 translation记录
     * 
     * @param row com.mybatis.gen_entity.Do.Translation
     * @return int
     *
     * @author lukenc
     */
    int insertSelective(Translation row);

    /**
     * 
     * 查询translation记录
     * 
     * @param id java.lang.Integer
     * @return Translation
     *
     * @author lukenc
     */
    Translation selectByPrimaryKey(Integer id);

    /**
     * 
     * 更新 translation记录
     * 
     * @param row com.mybatis.gen_entity.Do.Translation
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKeySelective(Translation row);

    /**
     * 
     * 更新 translation记录
     * 
     * @param row com.mybatis.gen_entity.Do.Translation
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKeyWithBLOBs(Translation row);

    /**
     * 
     * 更新 translation记录
     * 
     * @param row com.mybatis.gen_entity.Do.Translation
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKey(Translation row);


    List<Translation> getTranslationByLanguageId(@Param("languageId") Integer languageId);
    List<Translation> getTranslationByLanguageCode(@Param("languageCode") String  languageCode);
    List<Translation> getTranslationByContentId(@Param("contentId") Integer  contentId);

    List<IdCount> getContentTranslatedCount(@Param("contentIdList") List<Integer> contentIdList);

    Translation getTranslationByContentIdAndLanguage(@Param("contentId") Integer  contentId,@Param("languageId")Integer languageId);

}