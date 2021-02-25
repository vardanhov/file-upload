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


import java.util.Arrays;



@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    private UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/api/uploadFiles")
    public void uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Arrays.asList(files)
                .stream()
                .forEach(file -> uploadService.uploadFile(file));
    }

    //TODO нужна апишка которая будет принимать имя файла и возвращать true-false можно ли его добавлять.

    @ApiOperation(value = "загрузка файла")
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, @RequestBody boolean confidential) {
        return ResponseEntity.ok(uploadService.storeFile(file, confidential));

    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
