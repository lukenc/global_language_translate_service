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

    /**
     * 添加语言
     * @param param 语言添加参数
     * @return 操作结果
     */
    @PostMapping("addLanguage")
    public OperationResult add(@RequestBody LanguageAddParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        // 调用语言服务的添加方法
        int res= languageService.add(param.getName(),param.getIsoCode(),param.getNativeName(),userId);
        if (res>0){
            // 添加成功，返回操作成功结果
            return OperationResult.ok();
        }
        // 添加失败，返回操作失败结果
        return OperationResult.fail();
    }

    /**
     * 编辑语言
     * @param param 语言编辑参数
     * @return 操作结果
     */
    @PostMapping("editLanguage")
    public OperationResult editLanguage(@RequestBody LanguageEditParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        // 调用语言服务的编辑方法
        int res= languageService.edit(param.getId(),param.getName(),param.getIsoCode(),param.getNativeName(),userId);
        if (res>0){
            // 编辑成功，返回操作成功结果
            return OperationResult.ok();
        }
        // 编辑失败，返回操作失败结果
        return OperationResult.fail();
    }

    /**
     * 删除语言
     * @param param 删除参数
     * @return 操作结果
     */
    @PostMapping("deleteLanguage")
    public OperationResult deleteLanguage(@RequestBody DeleteParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        // 调用语言服务的删除方法
        int res= languageService.delete(param.getId(),userId);
        if (res>0){
            // 删除成功，返回操作成功结果
            return OperationResult.ok();
        }
        // 删除失败，返回操作失败结果
        return OperationResult.fail();
    }

    /**
     * 获取所有语言列表
     * @return 操作结果和语言列表
     */
    @GetMapping("getAllLanguageList")
    public OperationResult<List<LanguageBo>> getAllLanguageList(){
        // 调用语言服务的获取所有语言列表方法
        List<LanguageBo> languageBoList=  languageService.selectAllLanguageList();
        // 返回操作成功结果和语言列表
        return OperationResult.ok(languageBoList);
    }
}

