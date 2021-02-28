package com.example.uploadfile.controller;


import com.example.uploadfile.service.iface.UploadService;
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

    private UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }


    @ApiOperation(value = "загрузка файлов")
    @PostMapping(value="/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam(value="files", required=true) @NotNull @NotEmpty MultipartFile files[],
                           String path, Authentication authentication){
        uploadService.storeFiles(files, path, authentication);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "проверка прав")
    @PostMapping("/check-access")
    public void grantAccessById(Authentication authentication) {
        uploadService.checkUserUploadRights(authentication);
    }
}
