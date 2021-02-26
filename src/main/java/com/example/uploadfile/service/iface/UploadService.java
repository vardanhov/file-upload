package com.example.uploadfile.service.iface;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface UploadService {

    public List<File> storeFiles(MultipartFile[] multipartFileList, String path, Authentication authentication);
}
