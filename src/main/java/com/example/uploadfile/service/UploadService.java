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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UploadService {

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    @Value("${upload.path.scripts}")
    public String uploadDir;

    public List<File> storeFiles(MultipartFile[] multipartFileList){
       return Stream.of(multipartFileList).map(this::storeFile).collect(Collectors.toList());
    }
    public File storeFile(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new FileNotFoundException("Ни один файл не выбран! Такого не должно было случиться, но почему-то все равно случилось");
        }

        if (!Objects.equals(multipartFile.getContentType(), "text/x-python")) {
            throw new FileContentTypeException("Неверное содержимое/тип файла: " + multipartFile.getContentType());
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName == null) {
            throw new FileNameException("Некорректное имя файла");
        }
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
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }
        return file;
//        return new UploadFileResponse(fileName, multipartFile.getContentType(), multipartFile.getSize());
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

