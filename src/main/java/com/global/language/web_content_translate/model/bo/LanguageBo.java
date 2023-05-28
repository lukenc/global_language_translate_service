package com.global.language.web_content_translate.model.bo;

import com.global.language.web_content_translate.model.enity.Language;

import java.io.Serializable;

/**
 *   语言表，用于存储支持的语言信息
 *   表：language
 *
 * @author lukenc
 * @date 2023年05月18日 10:46:16
 */
public class LanguageBo implements Serializable {
    /**
     * 
     *  语言的唯一标识符
     *
     * 数据库字段： language.id
     *
     * @author lukenc
     */
    private Integer id;

    /**
     * 
     *  语言名称，运维人员管理使用
     *
     * 数据库字段： language.name
     *
     * @author lukenc
     */
    private String name;

    /**
     * 
     *  ISO语言代码
     *
     * 数据库字段： language.iso_code
     *
     * @author lukenc
     */
    private String isoCode;

    /**
     * 
     *  语言的本地名称,用于展示给用户找到自己的语言
     *
     * 数据库字段： language.native_name
     *
     * @author lukenc
     */
    private String nativeName;

    public LanguageBo() {
        super();
    }

    public LanguageBo(Language language) {
        this.id=language.getId();
        this.name=language.getName();
        this.isoCode=language.getIsoCode();
        this.nativeName=language.getNativeName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
}