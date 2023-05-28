package com.global.language.web_content_translate.service.impl;

import com.global.language.web_content_translate.model.WebContentException;
import com.global.language.web_content_translate.model.bo.WebContentBo;
import com.global.language.web_content_translate.model.enity.WebContent;
import com.global.language.web_content_translate.repository.WebContentMapper;
import com.global.language.web_content_translate.service.WebContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class WebContentServiceImpl implements WebContentService {
    @Autowired
    WebContentMapper webContentMapper;

    @Override
    public int add(String originalContent, String type, String url, String userId) {
        WebContent content = WebContent.create(originalContent, type, url);
        content.setCreatedAt(new Date(Instant.now().toEpochMilli()));
        content.setCreatedBy(userId);
        int res = 0 ;
        try {
           res= webContentMapper.insertSelective(content);
        }catch (DuplicateKeyException exception){
            res= WebContentException.Duplicate.getCode();
        }
        return res;
    }

    @Override
    public int addOrUpdate(WebContent webContent,String userId) {
        WebContent ori= webContentMapper.selectByUrl(webContent.getUrl());
       if (ori==null){
           try {
               webContent.setCreatedAt(new Date(Instant.now().toEpochMilli()));
               webContent.setCreatedBy(userId);
               return webContentMapper.insertSelective(webContent);
           }catch (DuplicateKeyException exception){
               return WebContentException.Duplicate.getCode();
           }
       }
       else {
           webContent.setId(ori.getId());
           webContent.setModifiedAt(new Date(Instant.now().toEpochMilli()));
           webContent.setModifiedBy(userId);
           return webContentMapper.updateByPrimaryKeySelective(webContent);
       }
    }

    @Override
    public int edit(Integer id, String originalContent, String type, String url, String userId) {
        WebContent content = WebContent.create(originalContent, type, url);
        content.setId(id);
        content.setModifiedAt(new Date(Instant.now().toEpochMilli()));
        content.setModifiedBy(userId);
        return webContentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public int delete(Integer id, String userId) {
        WebContent content = new WebContent();
        content.setId(id);
        content.setEnable(false);
        content.setModifiedBy(userId);
        content.setModifiedAt(new Date(Instant.now().toEpochMilli()));
        return webContentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public List<WebContentBo> getAllWebContent() {
        List<WebContent> webContentList = webContentMapper.getAllWebContent();
        return list2BoList(webContentList);
    }

    @Override
    public List<WebContentBo> getWebContentListByIdList(List<Integer> idList) {
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }
        List<WebContent> webContentList = webContentMapper.getWebContentListByIdList(idList);
        return list2BoList(webContentList);
    }

    @Override
    public WebContentBo getWebContentById(Integer id) {
        WebContent webContent= webContentMapper.selectByPrimaryKey(id);
        if (webContent!=null) {
            return new WebContentBo(webContent);
        }
        return null;
    }

    private List<WebContentBo> list2BoList(List<WebContent> webContentList) {
        return webContentList != null ? webContentList.stream().map(WebContentBo::new).toList() : List.of();
    }
}
