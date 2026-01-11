package com.global.language.web_content_translate.model.param.language;

/**
 * 语言新增参数，record 形式便于不可变建模。
 */
public record LanguageAddParam(String name, String isoCode, String nativeName) { }