package com.global.language.web_content_translate.service;

import com.global.language.web_content_translate.model.bo.WebContentBo;
import com.global.language.web_content_translate.model.enity.WebContent;

import java.util.List;

public interface WebContentService {
    int add(String originalContent,String type,String url,String userId);
    int addOrUpdate(WebContent webContent, String userId);

    int edit(Integer id,String originalContent,String type,String url,String userId);

    int delete(Integer id,String userId);

    List<WebContentBo> getAllWebContent();

    List<WebContentBo> getWebContentListByIdList(List<Integer> idList);

    WebContentBo getWebContentById(Integer id);

}
