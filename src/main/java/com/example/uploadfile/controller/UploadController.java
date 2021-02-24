package com.example.uploadfile.controller;


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

    //TODO через конструктор
    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "загрузка файла")
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, @RequestBody boolean confidential) {
        return ResponseEntity.ok(uploadService.storeFile(file,confidential));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
