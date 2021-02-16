package com.example.uploadfile.controller;


import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.service.UploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping( produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        return uploadService.storeFile(file);
    }
}
