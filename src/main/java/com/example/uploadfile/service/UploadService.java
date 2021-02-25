package com.example.uploadfile.service;


import com.example.uploadfile.dto.UploadFileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UploadService {

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    public void storeFiles(MultipartFile[] multipartFileList){
       Stream.of(multipartFileList).map(this::storeFile).collect(Collectors.toList());
    }
    public File storeFile(MultipartFile multipartFile) {
//        if (multipartFile == null) {
//            throw new FileNotFoundException("Cannot find file");
//        }
//
//        //Fixme
//        if (!Objects.equals(multipartFile.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
//            throw new FileContentTypeException("Invalid content type: " + multipartFile.getContentType());
//        }
//
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        if (fileName == null) {
//            throw new FileNameException("Invalid file name");
//        }
//
//        if (fileName.length() < 2 || !fileName.endsWith("py")) {
//            throw new FileNameException("Invalid file name");
//        }
        String path = pathOfRegularFiles;
        //Fixme - проверить проверку, вроде должно быть наоборот
//        if (!confidential)
//            path = pathOfConfidentialFiles;
        File file = new File(path + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }
//        return new UploadFileResponse(fileName, multipartFile.getContentType(), multipartFile.getSize());
        return file;
    }
}
