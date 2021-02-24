package com.example.uploadfile.service;


import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.excepion.FileContentTypeException;
import com.example.uploadfile.excepion.FileNameException;
import com.example.uploadfile.excepion.FileNotFoundException;
import com.example.uploadfile.excepion.FileStorageException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;


@Service
public class UploadService {

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    @Value("${upload.path.scripts}")
    public String uploadDir;


    public UploadFileResponse storeFile(MultipartFile multipartFile, boolean confidential) {
        if (multipartFile == null) {
            throw new FileNotFoundException("Cannot find file");
        }

        //Fixme
        if (!Objects.equals(multipartFile.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
            throw new FileContentTypeException("Invalid content type: " + multipartFile.getContentType());
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName == null) {
            throw new FileNameException("Invalid file name");
        }

        if (fileName.length() < 2 || !fileName.endsWith("py")) {
            throw new FileNameException("Invalid file name");
        }
        String path = pathOfRegularFiles;
        //Fixme - проверить проверку, вроде должно быть наоборот
        if (!confidential)
            path = pathOfConfidentialFiles;
        File file = new File(path + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }
        return new UploadFileResponse(fileName, multipartFile.getContentType(), multipartFile.getSize());
    }


    public void uploadFile(MultipartFile file) {
        try {
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("File Not Found");
        }
    }
}

