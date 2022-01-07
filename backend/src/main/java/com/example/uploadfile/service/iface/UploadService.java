package com.example.uploadfile.service.iface;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;


public interface UploadService {


    List<File> storeFiles(MultipartFile[] multipartFileList, Boolean isDag, String folder, Boolean canOverride, Authentication authentication);

    void checkUserUploadRights(Authentication authentication);

    Boolean checkUserUploadRightsOnEnter(Authentication authentication);

    Map<String, Boolean> fileAndFolderChecks(String fileName, Boolean isDag, String folder,
                                      Authentication authentication);


    }
