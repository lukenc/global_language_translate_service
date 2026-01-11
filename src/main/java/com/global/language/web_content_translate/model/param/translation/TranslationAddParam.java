package com.global.language.web_content_translate.model.param.translation;

/**
 * 翻译新增参数
 */
public record TranslationAddParam(Integer contentId, Integer languageId, String languageCode, String translatedText) { }