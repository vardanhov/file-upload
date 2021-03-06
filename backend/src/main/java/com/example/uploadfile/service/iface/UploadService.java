package com.example.uploadfile.service.iface;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public interface UploadService {

    List<File> storeFiles(MultipartFile[] multipartFileList, String path, Authentication authentication);
    void checkUserUploadRights(Authentication authentication);
    boolean checkUserUploadRightsOnEnter(Authentication authentication);
}
