package com.example.uploadfile.service;


import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.FileContentTypeException;
import com.example.uploadfile.excepion.FileNameException;
import com.example.uploadfile.excepion.FileNotFoundException;
import com.example.uploadfile.excepion.FileStorageException;

import com.example.uploadfile.repo.WhiteListUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.uploadfile.util.UserMapper.toWhiteListUserToDto;


@Service
public class UploadService {

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    @Value("${upload.path.scripts}")
    public String uploadDir;

    @Autowired
    WhiteListUserRepository whiteListUserRepository;

    public List<File> storeFiles(MultipartFile[] multipartFileList, Authentication authentication){
        checkUserUploadRights(authentication);
       return Stream.of(multipartFileList).map(this::storeFile).collect(Collectors.toList());
    }
    public File storeFile(MultipartFile multipartFile) {

        if (multipartFile == null) {
            throw new FileNotFoundException("Файл отсутствует");
        }
//
//        //Fixme
//        if (!Objects.equals(multipartFile.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
//            throw new FileContentTypeException("Invalid content type: " + multipartFile.getContentType());
//        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        if (fileName == null) {
//            throw new FileNameException("Invalid file name");
//        }
//
//        if (fileName.length() < 2 || !fileName.endsWith("py")) {
//            throw new FileNameException("Invalid file name");
//        }
        String path = pathOfRegularFiles;
//        //Fixme - проверить проверку, вроде должно быть наоборот
//        if (!confidential)
//            path = pathOfConfidentialFiles;
        File file = new File(path + multipartFile.getOriginalFilename());
        if(file.exists()) throw new FileNameException("Файл с таким именем уже существует, выберите другое имя");
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Файл %s не может не сохранен. Попробуйте еще раз!", fileName));
        }
        return file;
//        return new UploadFileResponse(fileName, multipartFile.getContentType(), multipartFile.getSize());
    }

    public boolean isFileExists(File fileName){

        return true;
    }


    public void uploadFile(MultipartFile file, Authentication authentication) {
        try {
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("File Not Found");
        }
    }

    public boolean isOwnerSame(String username, Path path){
        boolean isOwnerSame = false;
        FileOwnerAttributeView ownerInfo = Files.getFileAttributeView(path,  FileOwnerAttributeView.class);
        UserPrincipal fileOwner = null;
        try {
            fileOwner = ownerInfo.getOwner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ownerName =fileOwner.getName();
        if (ownerName.equals(username))
            isOwnerSame =true;
        return isOwnerSame;
    }

    //TODO поправить работу со временем
    public void checkUserUploadRights(Authentication authentication){
        WhiteListUserDto whiteListUserDto = toWhiteListUserToDto(whiteListUserRepository.getWhiteListUserByUserName(authentication.getName()));
//        if (!(whiteListUserDto.getFrom()/1000<System.currentTimeMillis()/1000) && (whiteListUserDto.getTo()>System.currentTimeMillis()/1000))
        if(true)
            throw new RuntimeException("Недостаточно прав");;
    }
}

