package com.global.language.web_content_translate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.WebContentException;
import com.global.language.web_content_translate.model.bo.WebContentBo;
import com.global.language.web_content_translate.model.enity.WebContent;
import com.global.language.web_content_translate.model.param.DeleteParam;
import com.global.language.web_content_translate.model.param.webContent.WebContentAddParam;
import com.global.language.web_content_translate.model.param.webContent.WebContentEditParam;
import com.global.language.web_content_translate.service.TranslationService;
import com.global.language.web_content_translate.service.WebContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("/webContent")
public class WebContentController {
    @Autowired
    WebContentService webContentService;
    @Autowired
    TranslationService translationService;

    /**
     * 添加网页内容
     *
     * @param param 网页内容参数
     * @return 操作结果
     */
    @PostMapping("addWebContent")
    public OperationResult<Void> addWebContent(@RequestBody WebContentAddParam param) {
        //todo 通过认证信息获取用户信息id
        String userId = "admin";
        int res = webContentService.add(param.getOriginalContent(), param.getType(), param.getUrl(), userId);
        if (res > 0) {
            return OperationResult.ok();
        } else {
            WebContentException exception = WebContentException.getException(res);
            return OperationResult.fail(exception.getCode(), exception.getMessage());
        }

    }

    /**
     * 编辑网页内容
     *
     * @param param 网页内容参数
     * @return 操作结果
     */
    @PostMapping("editWebContent")
    public OperationResult<Void> editWebContent(@RequestBody WebContentEditParam param) {
        //todo 通过认证信息获取用户信息id
        String userId = "admin";
        int res = webContentService.edit(param.getId(), param.getOriginalContent(), param.getType(), param.getUrl(), userId);
        if (res > 0) {
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    /**
     * 删除网页内容
     *
     * @param param 删除参数
     * @return 操作结果
     */
    @PostMapping("deleteWebContent")
    public OperationResult<Void> deleteLanguage(@RequestBody DeleteParam param) {
        //todo 通过认证信息获取用户信息id
        String userId = "admin";
        int res = webContentService.delete(param.getId(), userId);
        if (res > 0) {
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    /**
     * 获取所有网页内容
     *
     * @return 操作结果，包含网页内容列表
     */
    @GetMapping("getAllWebContent")
    public OperationResult<List<WebContentBo>> getAllWebContent() {
        List<WebContentBo> webContentBoList = webContentService.getAllWebContent();
        if (!CollectionUtils.isEmpty(webContentBoList)) {
            List<Integer> contentIdList = webContentBoList.stream().map(WebContentBo::getId).collect(Collectors.toList());
            if (contentIdList != null) {
                Map<Integer, Integer> content2TranslatedCountMap = translationService.getContentTranslatedCount(contentIdList);
                webContentBoList.forEach(webContentBo -> webContentBo.setTranslatedCount(content2TranslatedCountMap.get(webContentBo.getId())));
            }
        }
        return OperationResult.ok(webContentBoList);
    }

    /**
     * 导入网页内容
     *
     * @param file 导入的文件
     * @return 操作结果，包含导入失败的内容
     * @throws IOException IO异常
     */
    @PostMapping(value = "/importContent", consumes = "multipart/form-data")
    public OperationResult<?> importContent(@RequestPart("file") MultipartFile file) throws IOException {
        //todo 通过认证信息获取用户信息id
        String userId = "admin";
        StringBuilder exceptionSb = new StringBuilder();
        exceptionSb.append("""
                如下内容导入失败：
                                        
                """);
        // 获取文件名称
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = fileName.split("\\.")[0];
        byte[] fileContent = file.getBytes();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.readValue(fileContent, Map.class);

        // 遍历JSON的字段
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            String url = entry.getKey();
            String content = entry.getValue();
            WebContent webContent = WebContent.create(content, fileName, url);
            //
            int res = webContentService.addOrUpdate(webContent, userId);
            if (res < 0) {
                WebContentException exception = WebContentException.getException(res);
                String result = """
                        URL：%s--描述：%s 原因：%s
                        """.formatted(webContent.getUrl(), webContent.getOriginalContent(), exception.getMessage());
                exceptionSb.append(result);
            }
        }

        return OperationResult.ok(exceptionSb.toString());
    }
}
