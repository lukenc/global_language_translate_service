package com.global.language.web_content_translate.service;

import com.global.language.web_content_translate.model.bo.WebContentBo;
import com.global.language.web_content_translate.model.enity.WebContent;

import java.util.List;

/**
 * WebContentService 接口定义了对 Web 内容的操作方法。
 */
public interface WebContentService {

    /**
     * 添加 Web 内容。
     *
     * @param originalContent 原始内容
     * @param type            类型
     * @param url             URL
     * @param userId          用户ID
     * @return 添加的结果，成功返回1，失败返回0
     */
    int add(String originalContent, String type, String url, String userId);

    /**
     * 添加或更新 Web 内容。
     *
     * @param webContent Web 内容对象
     * @param userId     用户ID
     * @return 添加或更新的结果，成功返回1，失败返回0
     */
    int addOrUpdate(WebContent webContent, String userId);

    /**
     * 编辑 Web 内容。
     *
     * @param id              内容ID
     * @param originalContent 原始内容
     * @param type            类型
     * @param url             URL
     * @param userId          用户ID
     * @return 编辑的结果，成功返回1，失败返回0
     */
    int edit(Integer id, String originalContent, String type, String url, String userId);

    /**
     * 删除 Web 内容。
     *
     * @param id     内容ID
     * @param userId 用户ID
     * @return 删除的结果，成功返回1，失败返回0
     */
    int delete(Integer id, String userId);

    /**
     * 获取所有的 Web 内容。
     *
     * @return 所有的 Web 内容列表
     */
    List<WebContentBo> getAllWebContent();

    /**
     * 根据ID列表获取 Web 内容列表。
     *
     * @param idList ID列表
     * @return 对应的 Web 内容列表
     */
    List<WebContentBo> getWebContentListByIdList(List<Integer> idList);

    /**
     * 根据ID获取 Web 内容。
     *
     * @param id 内容ID
     * @return 对应的 Web 内容对象
     */
    WebContentBo getWebContentById(Integer id);

}