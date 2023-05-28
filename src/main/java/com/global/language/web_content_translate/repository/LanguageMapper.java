package com.global.language.web_content_translate.repository;


import com.global.language.web_content_translate.model.enity.Language;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageMapper {
    /**
     * 
     * 删除 language记录
     * 
     * @param id java.lang.Integer
     * @return int
     *
     * @author lukenc
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 
     * 新建 language记录
     * 
     * @param row com.mybatis.gen_entity.Do.Language
     * @return int
     *
     * @author lukenc
     */
    int insert(Language row);

    /**
     * 
     * 新建 language记录
     * 
     * @param row com.mybatis.gen_entity.Do.Language
     * @return int
     *
     * @author lukenc
     */
    int insertSelective(Language row);

    /**
     * 
     * 查询language记录
     * 
     * @param id java.lang.Integer
     * @return Language
     *
     * @author lukenc
     */
    Language selectByPrimaryKey(Integer id);

    Language selectByIso(@Param("iso") String  iso);


    /**
     * 
     * 更新 language记录
     * 
     * @param row com.mybatis.gen_entity.Do.Language
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKeySelective(Language row);

    /**
     * 
     * 更新 language记录
     * 
     * @param row com.mybatis.gen_entity.Do.Language
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKey(Language row);

    /**
    * 逻辑删除
     *
    * @date 2023/5/18 11:21 下午
     * @param id
     * @return int
    */
    int delete(@Param("id") Integer id,@Param("modifiedBy") String modifiedBy);

    List<Language> getAllLanguageList();
}