package com.global.language.web_content_translate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.language.web_content_translate.model.OperationResult;
import com.global.language.web_content_translate.model.bo.TranslationBo;
import com.global.language.web_content_translate.model.param.DeleteParam;
import com.global.language.web_content_translate.model.param.translation.TranslationAddParam;
import com.global.language.web_content_translate.model.param.translation.TranslationEditParam;
import com.global.language.web_content_translate.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController("translation")
public class TranslationController {
    @Autowired
    TranslationService translationService;

    @GetMapping("getTranslationListByContentId")
    public OperationResult<List<TranslationBo>> getTranslationListByContentId(Integer contentId){
        List<TranslationBo> translationBoList=translationService.getTranslationByContentId(contentId);
        if (translationBoList!=null){
            return OperationResult.ok(translationBoList);
        }
        return OperationResult.ok();
    }

    @PostMapping("addTranslation")
    public OperationResult<Void> addTranslation(@RequestBody TranslationAddParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= translationService.add(param.getLanguageCode(),param.getLanguageId(),param.getContentId(),param.getTranslatedText(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    @PostMapping("editTranslation")
    public OperationResult<Void> editTranslation(@RequestBody TranslationEditParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= translationService.edit(param.getId(),param.getLanguageCode(),param.getLanguageId(),param.getContentId(),param.getTranslatedText(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    @PostMapping("deleteTranslation")
    public OperationResult<Void> editTranslation(@RequestBody DeleteParam param){
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        int res= translationService.delete(param.getId(),userId);
        if (res>0){
            return OperationResult.ok();
        }
        return OperationResult.fail();
    }

    @GetMapping("getTranslationByLanguageCode")
    public ResponseEntity<FileSystemResource> getTranslationByLanguageCode(String   languageCode) throws IOException{
        List<TranslationBo> res= translationService.getTranslationByLanguageCode(languageCode);
        Map<String,List<TranslationBo>> type2TranslationsMap=res.stream().filter( o->o.getContentType()!=null).collect(Collectors.groupingBy(TranslationBo::getContentType));
        List<String > files=new ArrayList<>();
        String zipFilename=languageCode+".zip";
        if (type2TranslationsMap!=null&&!type2TranslationsMap.isEmpty()){
            type2TranslationsMap.forEach((k,v)->{
                var filename = k+".json";
                var content = "{}";
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

    @GetMapping("getTranslationByLanguageId")
    public OperationResult<List<TranslationBo>> getTranslationByLanguageId(Integer  languageId){

        List<TranslationBo> res= translationService.getTranslationByLanguageId(languageId);
        if (res!=null){
            return OperationResult.ok(res);
        }
        return OperationResult.fail();
    }



    @PostMapping(value = "/importTranslation",consumes ="multipart/form-data")
    public OperationResult<?> importTranslation(@RequestPart("file") MultipartFile file ) {
        //todo 通过认证信息获取用户信息id
        String userId="admin";
        return translationService.importTranslations(file,userId);
    }




    private void createFile(String filename, String content) throws IOException {
        Path path = Paths.get(filename);
        Files.write(path, content.getBytes());
    }


    private void zipFiles(List<String> filenames, String zipFilename) throws IOException {
        try (var zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilename)))) {
            for (var filename : filenames) {
                var filePath = Paths.get(filename);
                zos.putNextEntry(new ZipEntry(filePath.getFileName().toString()));
                Files.copy(filePath, zos);
                zos.closeEntry();
            }
        }
    }



}
