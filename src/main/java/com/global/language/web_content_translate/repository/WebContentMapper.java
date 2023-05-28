package com.global.language.web_content_translate.repository;


import com.global.language.web_content_translate.model.enity.WebContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebContentMapper {
    /**
     * 
     * 删除 web_content记录
     * 
     * @param id java.lang.Integer
     * @return int
     *
     * @author lukenc
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 
     * 新建 web_content记录
     * 
     * @param row com.mybatis.gen_entity.Do.WebContent
     * @return int
     *
     * @author lukenc
     */
    int insert(WebContent row);

    /**
     * 
     * 新建 web_content记录
     * 
     * @param row com.mybatis.gen_entity.Do.WebContent
     * @return int
     *
     * @author lukenc
     */
    int insertSelective(WebContent row);

    /**
     * 
     * 查询web_content记录
     * 
     * @param id java.lang.Integer
     * @return WebContent
     *
     * @author lukenc
     */
    WebContent selectByPrimaryKey(Integer id);

    /**
     * 
     * 更新 web_content记录
     * 
     * @param row com.mybatis.gen_entity.Do.WebContent
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKeySelective(WebContent row);

    /**
     * 
     * 更新 web_content记录
     * 
     * @param row com.mybatis.gen_entity.Do.WebContent
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKeyWithBLOBs(WebContent row);

    /**
     * 
     * 更新 web_content记录
     * 
     * @param row com.mybatis.gen_entity.Do.WebContent
     * @return int
     *
     * @author lukenc
     */
    int updateByPrimaryKey(WebContent row);

    List<WebContent> getAllWebContent();

    List<WebContent> getWebContentListByIdList(@Param("idList") List<Integer> idList);

    WebContent selectByUrl(@Param("url") String url);


}