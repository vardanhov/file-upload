package com.example.uploadfile.controller;


import com.example.uploadfile.service.UploadServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    private UploadServiceImpl uploadServiceImpl;

    @Autowired
    public UploadController(UploadServiceImpl uploadServiceImpl) {
        this.uploadServiceImpl = uploadServiceImpl;
    }

//    @PostMapping("/api/uploadFiles")
//    public void uploadFiles(@RequestParam("files") MultipartFile[] files) {
//        Arrays.asList(files)
//                .stream()
//                .forEach(file -> uploadService.uploadFile(file));
//    }

    @ApiOperation(value = "загрузка файлов")
    @PostMapping(value="/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam(value="files", required=true) @NotNull @NotEmpty MultipartFile files[],
                           String path, Authentication authentication){
        uploadServiceImpl.storeFiles(files, path, authentication);
    }

    @ApiOperation(value = "разрешенные типы файлов из настроек приложения")
    @GetMapping(value="/api/upload/contentType")
    public ResponseEntity<String> getAllowedContentTypes(Authentication authentication){
        return ResponseEntity.ok(uploadServiceImpl.getAllowedContentTypes(authentication));
    }

    //TODO нужна апишка которая будет принимать имя файла и возвращать true-false можно ли его добавлять.


    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
