package com.example.uploadfile.controller;


import com.example.uploadfile.service.UploadService;
import io.swagger.annotations.ApiOperation;
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping( produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    private UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    //TODO нужна апишка которая будет принимать имя файла и возвращать true-false можно ли его добавлять.
    //TODO нужна апишка для множественного добавления файлов (в папку Script мы можем добавлять по несколько файлов)


    @ApiOperation(value = "загрузка файла")
    @PostMapping("/api/upload")
    public void uploadFiles(@RequestBody List<MultipartFile> files) {
        uploadService.storeFiles(files);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
