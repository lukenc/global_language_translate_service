package com.global.language.web_content_translate.service;

import com.global.language.web_content_translate.model.bo.LanguageBo;
import com.global.language.web_content_translate.model.enity.Language;

import java.util.List;

/**
 * LanguageService接口定义了对语言信息的操作方法。
 */
public interface LanguageService {

    /**
     * 添加一种新的语言。
     *
     * @param name 语言名称
     * @param isoCode 语言ISO代码
     * @param nativeName 语言本地名称
     * @param userId 用户ID
     * @return 返回添加操作的结果，成功返回1，失败返回0
     */
    int add(String name, String isoCode, String nativeName, String userId);

    /**
     * 编辑指定ID的语言信息。
     *
     * @param id 语言ID
     * @param name 语言名称
     * @param isoCode 语言ISO代码
     * @param nativeName 语言本地名称
     * @param userId 用户ID
     * @return 返回编辑操作的结果，成功返回1，失败返回0
     */
    int edit(Integer id, String name, String isoCode, String nativeName, String userId);

    /**
     * 删除指定ID的语言信息。
     *
     * @param id 语言ID
     * @param userId 用户ID
     * @return 返回删除操作的结果，成功返回1，失败返回0
     */
    int delete(Integer id, String userId);

    /**
     * 获取所有语言信息的列表。
     *
     * @return 返回所有语言信息的列表
     */
    List<LanguageBo> selectAllLanguageList();

    /**
     * 根据ISO代码获取语言信息。
     *
     * @param iso 语言ISO代码
     * @return 返回对应ISO代码的语言信息
     */
    Language getLanguageByIso(String iso);
}
