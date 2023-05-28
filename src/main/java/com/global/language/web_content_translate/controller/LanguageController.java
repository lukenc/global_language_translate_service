package com.global.language.web_content_translate.controller;

import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.bo.LanguageBo;
import com.global.language.web_content_translate.model.param.DeleteParam;
import com.global.language.web_content_translate.model.param.language.LanguageAddParam;
import com.global.language.web_content_translate.model.param.language.LanguageEditParam;
import com.global.language.web_content_translate.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("language")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @PostMapping("addLanguage")
    public OperationResult add(@RequestBody LanguageAddParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
       int res= languageService.add(param.getName(),param.getIsoCode(),param.getNativeName(),userId);
       if (res>0){
           return OperationResult.ok();
       }
       return OperationResult.fail();
    }


    @PostMapping("editLanguage")
    public OperationResult editLanguage(@RequestBody LanguageEditParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= languageService.edit(param.getId(),param.getName(),param.getIsoCode(),param.getNativeName(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    @PostMapping("deleteLanguage")
    public OperationResult deleteLanguage(@RequestBody DeleteParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= languageService.delete(param.getId(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }


    @GetMapping("getAllLanguageList")
    public OperationResult<List<LanguageBo>> getAllLanguageList(){
      List<LanguageBo> languageBoList=  languageService.selectAllLanguageList();
      return OperationResult.ok(languageBoList);
    }

}
