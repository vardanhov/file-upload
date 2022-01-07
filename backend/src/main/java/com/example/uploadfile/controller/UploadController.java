package com.example.uploadfile.controller;


import com.example.uploadfile.service.iface.UploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }


    @ApiOperation(value = "загрузка файлов")
    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFiles(@RequestParam(value = "files", required = true) @NotNull @NotEmpty MultipartFile[] files,
                            @RequestParam Boolean isDag,
                            @RequestParam String folder,
                            @RequestParam Boolean toOverride,
                            Authentication authentication) {
        uploadService.storeFiles(files, isDag, folder, toOverride, authentication);
    }


    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    //Fixme - не забыть удалить после того, как доделаем шедулер
    @ApiOperation(value = "проверка прав")
    @GetMapping("/api/upload/check-access")
    public ResponseEntity<Boolean> checkAccess(Authentication authentication) {
        return ResponseEntity.ok(uploadService.checkUserUploadRightsOnEnter(authentication));
    }

    @ApiOperation(value = "проверка существования и доступов к папке, файлу")
    @PostMapping("api/upload/check-file")
    public ResponseEntity<Map<String, Boolean>> check(@RequestParam String fileName,
                                                      @RequestParam String folder,
                                                      @RequestParam Boolean isDag,
                                                      Authentication authentication) {
        return ResponseEntity.ok(uploadService.fileAndFolderChecks(fileName, isDag, folder, authentication));
    }
}
