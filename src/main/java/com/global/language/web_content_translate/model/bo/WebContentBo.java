package com.global.language.web_content_translate.model.bo;

import com.global.language.web_content_translate.model.enity.WebContent;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

/**
 *   网页内容表，用于存储网页的原始内容和相关信息
 *   表：web_content
 *
 * @author lukenc
 * @date 2023年05月18日 10:47:08
 */
public class WebContentBo implements Serializable {
    /**
     * 
     *  网页内容的唯一标识符
     *
     * 数据库字段： web_content.id
     *
     * @author lukenc
     */
    private Integer id;

    /**
     * 
     *  网页的URL
     *
     * 数据库字段： web_content.url
     *
     * @author lukenc
     */
    private String url;

    private Integer translatedCount=0;
    /**
     *
     *  创建人
     *
     * 数据库字段： web_content.created_by
     *
     * @author lukenc
     */
    private String createdBy;

    /**
     *
     *  创建时间
     *
     * 数据库字段： web_content.created_at
     *
     * @author lukenc
     */
    private Date createdAt;

    /**
     *
     *  最后修改人
     *
     * 数据库字段： web_content.modified_by
     *
     * @author lukenc
     */
    private String modifiedBy;

    /**
     *
     *  最后修改时间
     *
     * 数据库字段： web_content.modified_at
     *
     * @author lukenc
     */
    private Date modifiedAt;

    /**
     * 
     *  例如button，list
     *
     * 数据库字段： web_content.type
     *
     * @author lukenc
     */
    private String type;


    /**
     * 
     *  原始网页内容
     *
     * 数据库字段： web_content.original_content
     *
     * @author lukenc
     */
    private String originalContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table web_content
     *
     * @author lukenc
     */
    private static final long serialVersionUID = 1L;

    public WebContentBo() {
        super();
    }
    public WebContentBo(WebContent content) {
        this.id=content.getId();
        this.createdAt=content.getCreatedAt();
        this.createdBy=content.getCreatedBy();
        this.modifiedAt=content.getModifiedAt();
        this.modifiedBy=content.getModifiedBy();
        this.originalContent=content.getOriginalContent();
        this.type=content.getType();
        this.url= content.getUrl();
    }

    /**
     * 获取web_content.id
     *
     * @return the value of web_content.id
     *
     * @author lukenc
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置web_content.id
     *
     * @param id the value for web_content.id
     *
     * @author lukenc
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取web_content.url
     *
     * @return the value of web_content.url
     *
     * @author lukenc
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置web_content.url
     *
     * @param url the value for web_content.url
     *
     * @author lukenc
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取web_content.created_by
     *
     * @return the value of web_content.created_by
     *
     * @author lukenc
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置web_content.created_by
     *
     * @param createdBy the value for web_content.created_by
     *
     * @author lukenc
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * 获取web_content.created_at
     *
     * @return the value of web_content.created_at
     *
     * @author lukenc
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置web_content.created_at
     *
     * @param createdAt the value for web_content.created_at
     *
     * @author lukenc
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取web_content.modified_by
     *
     * @return the value of web_content.modified_by
     *
     * @author lukenc
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置web_content.modified_by
     *
     * @param modifiedBy the value for web_content.modified_by
     *
     * @author lukenc
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy == null ? null : modifiedBy.trim();
    }

    /**
     * 获取web_content.modified_at
     *
     * @return the value of web_content.modified_at
     *
     * @author lukenc
     */
    public Date getModifiedAt() {
        return modifiedAt;
    }

    /**
     * 设置web_content.modified_at
     *
     * @param modifiedAt the value for web_content.modified_at
     *
     * @author lukenc
     */
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    /**
     * 获取web_content.type
     *
     * @return the value of web_content.type
     *
     * @author lukenc
     */
    public String getType() {
        return type;
    }

    /**
     * 设置web_content.type
     *
     * @param type the value for web_content.type
     *
     * @author lukenc
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }


    /**
     * 获取web_content.original_content
     *
     * @return the value of web_content.original_content
     *
     * @author lukenc
     */
    public String getOriginalContent() {
        return originalContent;
    }

    /**
     * 设置web_content.original_content
     *
     * @param originalContent the value for web_content.original_content
     *
     * @author lukenc
     */
    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent == null ? null : originalContent.trim();
    }

    public static WebContentBo create(String originalContent, String type, String url){
        WebContentBo content=new WebContentBo();
        content.originalContent=originalContent;
        content.type=type;
        content.url=url;
        return content;
    }

    public Integer getTranslatedCount() {
        return translatedCount;
    }

    public void setTranslatedCount(Integer translatedCount) {
        if (translatedCount!=null) {
            this.translatedCount = translatedCount;
        }
    }

    /**
     * 转字符
     *
     * @return String
     * @author lukenc
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", WebContentBo.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("url='" + url + "'")
                .add("translatedCount=" + translatedCount)
                .add("createdBy='" + createdBy + "'")
                .add("createdAt=" + createdAt)
                .add("modifiedBy='" + modifiedBy + "'")
                .add("modifiedAt=" + modifiedAt)
                .add("type='" + type + "'")
                .add("originalContent='" + originalContent + "'")
                .toString();
    }
}