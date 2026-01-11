package com.global.language.web_content_translate.model.param.translation;

/**
 * 翻译编辑参数
 */
public record TranslationEditParam(Integer id, Integer contentId, Integer languageId, String languageCode, String translatedText) { }