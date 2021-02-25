package com.example.uploadfile.controller;


import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.service.UploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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


    ////Fixme сделать обработку массива файлов
    @PostMapping(value="/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam(value="files", required=true) MultipartFile files[]){
        uploadService.storeFiles(files);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
