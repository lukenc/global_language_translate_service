package com.global.language.web_content_translate.model.param.webContent;

/**
 * 新建网页内容表的请求参数
 */
public record WebContentAddParam(String url, String type, String originalContent) { }