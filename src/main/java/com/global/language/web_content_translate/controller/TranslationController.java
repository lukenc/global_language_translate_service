package com.global.language.web_content_translate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.bo.TranslationBo;
import com.global.language.web_content_translate.model.param.DeleteParam;
import com.global.language.web_content_translate.model.param.translation.TranslationAddParam;
import com.global.language.web_content_translate.model.param.translation.TranslationEditParam;
import com.global.language.web_content_translate.service.TranslationService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/translation")
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * 根据内容ID获取翻译列表
     * @param contentId 内容ID
     * @return 返回翻译列表
     */
    @GetMapping("getTranslationListByContentId")
    public OperationResult<List<TranslationBo>> getTranslationListByContentId(Integer contentId){
        List<TranslationBo> translationBoList=translationService.getTranslationByContentId(contentId);
        if (translationBoList!=null){
            return OperationResult.ok(translationBoList);
        }
        return OperationResult.ok();
    }

    /**
     * 添加翻译
     * @param param 翻译参数
     * @return 返回操作结果
     */
    @PostMapping("addTranslation")
    public OperationResult<Void> addTranslation(@RequestBody TranslationAddParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= translationService.add(param.languageCode(),param.languageId(),param.contentId(),param.translatedText(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    /**
     * 编辑翻译
     * @param param 翻译参数
     * @return 返回操作结果
     */
    @PostMapping("editTranslation")
    public OperationResult<Void> editTranslation(@RequestBody TranslationEditParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= translationService.edit(param.id(),param.languageCode(),param.languageId(),param.contentId(),param.translatedText(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    /**
     * 删除翻译
     * @param param 删除参数
     * @return 返回操作结果
     */
    @PostMapping("deleteTranslation")
    public OperationResult<Void> editTranslation(@RequestBody DeleteParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= translationService.delete(param.id(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    /**
     * 根据语言代码获取翻译
     * @param languageCode 语言代码
     * @return 返回翻译文件
     * @throws IOException 抛出IO异常
     */
    @GetMapping("getTranslationByLanguageCode")
    public ResponseEntity<FileSystemResource> getTranslationByLanguageCode(String languageCode) throws IOException{
        List<TranslationBo> res= translationService.getTranslationByLanguageCode(languageCode);
        Map<String,List<TranslationBo>> type2TranslationsMap=res.stream().filter( o->o.getContentType()!=null).collect(Collectors.groupingBy(TranslationBo::getContentType));
        List<String > files=new ArrayList<>();
        String zipFilename=languageCode+".zip";
        if (type2TranslationsMap!=null&&!type2TranslationsMap.isEmpty()){
            type2TranslationsMap.forEach((k,v)->{
                String filename = k+".json";
                String content = "{}";
                files.add(filename);
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String,String> content2TranslationMap=v.stream().collect(Collectors.toMap(TranslationBo::getContentUrl,TranslationBo::getTranslatedText));
                try {
                    content=  objectMapper.writeValueAsString(content2TranslationMap);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                try {
                    createFile(filename, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        zipFiles(files, zipFilename);
        // 构建压缩包的文件系统资源
        FileSystemResource resource = new FileSystemResource(zipFilename);

        // 设置响应头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", zipFilename);

        // 返回压缩包的响应实体
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    /**
     * 根据语言ID获取翻译列表
     * @param languageId 语言ID
     * @return 返回翻译列表
     */
    @GetMapping("getTranslationByLanguageId")
    public OperationResult<List<TranslationBo>> getTranslationByLanguageId(Integer languageId){

        List<TranslationBo> res= translationService.getTranslationByLanguageId(languageId);
        if (res!=null){
            return OperationResult.ok(res);
        }
        return OperationResult.fail();
    }

    /**
     * 导入翻译
     * @param file 翻译文件
     * @return 返回操作结果
     */
    @PostMapping(value = "/importTranslation",consumes ="multipart/form-data")
    public OperationResult<?> importTranslation(@RequestPart("file") MultipartFile file ) {
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        return translationService.importTranslations(file,userId);
    }

    /**
     * 创建文件
     * @param filename 文件名
     * @param content 文件内容
     * @throws IOException 抛出IO异常
     */
    private void createFile(String filename, String content) throws IOException {
        Path path = Paths.get(filename);
        Files.write(path, content.getBytes());
    }

    /**
     * 压缩文件
     * @param filenames 文件名列表
     * @param zipFilename 压缩文件名
     * @throws IOException 抛出IO异常
     */
    private void zipFiles(List<String> filenames, String zipFilename) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilename)))) {
            for (String filename : filenames) {
                Path filePath = Paths.get(filename);
                zos.putNextEntry(new ZipEntry(filePath.getFileName().toString()));
                Files.copy(filePath, zos);
                zos.closeEntry();
            }
        }
    }
}

