package com.example.uploadfile.service;


import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.excepion.FileContentTypeException;
import com.example.uploadfile.excepion.FileNameException;
import com.example.uploadfile.excepion.FileNotFoundException;
import com.example.uploadfile.excepion.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Service
public class UploadService {

    @Value("upload.path")
    private String path;


    public UploadFileResponse storeFile(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new FileNotFoundException("Cannot find file");
        }

        if (!"py".equals(multipartFile.getContentType())) {
            throw new FileContentTypeException("invalid content type");
        }

        if (multipartFile.getOriginalFilename()== null) {
            throw new FileNameException("Invalid file name");
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        File file = new File(path + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }

        return new UploadFileResponse(fileName, multipartFile.getContentType(), multipartFile.getSize());

    }
}