package com.example.uploadfile.service;


import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.FileNameException;
import com.example.uploadfile.excepion.FileNotFoundException;
import com.example.uploadfile.excepion.FileStorageException;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.service.iface.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.uploadfile.util.UserMapper.toLocalDate;
import static com.example.uploadfile.util.UserMapper.toWhiteListUserDto;


@Service
public class UploadServiceImpl implements UploadService {

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    @Value("${upload.path.scripts}")
    public String uploadDir;

    @Value("${allowed.file.content.types}")
    String allowedContentTypes;

    WhiteListUserRepository whiteListUserRepository;

    @Autowired
    public UploadServiceImpl(WhiteListUserRepository whiteListUserRepository) {
        this.whiteListUserRepository = whiteListUserRepository;
    }

    public List<File> storeFiles(MultipartFile[] multipartFileList, String path, Authentication authentication) {
        checkUserUploadRights(authentication);
        return Stream.of(multipartFileList).map(multipartFile -> storeFile(multipartFile, path, authentication)).collect(Collectors.toList());
    }

    //TODO еще поработать с этим методом
    public File storeFile(MultipartFile multipartFile, String path, Authentication authentication) {
       //TODO сделать проверку что кастомный путь заканчивается на слеш или нет
//
//
//        File filePath = new File(returnTargetPath(path,authentication));
//
//        try{
//            if(filePath.mkdir()) {
//                System.out.println("Directory Created");
//            } else {
//
//                System.out.println("Directory is not created");
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        if (multipartFile == null) { throw new FileNotFoundException("Файл отсутствует"); }
        Path pathSaveFileTo = returnTargetPath(path,authentication);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if (fileName == null || fileName.length() < 4) { throw new FileNameException("Некорректное имя файла"); }
        //TODO из настроек массив допустимых типов файлов
//        if (!fileName.endsWith(".py")) { throw new FileNameException("Неверный формат файла"); }

        File file = new File(pathSaveFileTo.toString(), multipartFile.getOriginalFilename());

        if (file.exists() && !isOwnerSame(authentication.getName(), pathSaveFileTo)) throw new FileNameException("Файл с таким именем уже существует, выберите другое имя");
        else if (!file.exists() ){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new FileStorageException(String.format("Файл не может быть сохранен. Второй раз можно не пробовать!", fileName));
            }
        }
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Файл не может быть сохранен. Попробуйте еще раз!", fileName));
        }
        return file;
        //Fixme
//        if (!Objects.equals(multipartFile.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
//            throw new FileContentTypeException("Invalid content type: " + multipartFile.getContentType());
//        }
    }

//Fixme
    public boolean isOwnerSame(String username, Path path) {
        boolean isOwnerSame = false;
        FileOwnerAttributeView ownerInfo = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        UserPrincipal fileOwner = null;
        try {
            fileOwner = ownerInfo.getOwner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ownerName = fileOwner.getName();
        if (ownerName.equals(username))
            isOwnerSame = true;
        return isOwnerSame;
    }

//Fixme
    public void checkUserUploadRights(Authentication authentication) {
        WhiteListUserDto whiteListUserDto = null;
                try{
                    whiteListUserDto = toWhiteListUserDto(whiteListUserRepository.getWhiteListUserByUserName(authentication.getName()));
                } catch (Exception e)
                {
                    throw new RuntimeException("Анонимный пользователь");
                }
        if (!isWithinRange(whiteListUserDto.getFrom(), whiteListUserDto.getTo()))
            throw new RuntimeException("Недостаточно прав для загрузки");
    }

    private boolean isWithinRange(LocalDateTime from, LocalDateTime to) {
        return (from.isBefore(toLocalDate(System.currentTimeMillis()))
                && to.isAfter(toLocalDate(System.currentTimeMillis())));
    }

    public String getAllowedContentTypes(Authentication authentication){
        checkUserUploadRights(authentication);
        return allowedContentTypes;
    }

    private Path returnTargetPath(String path, Authentication authentication){
        return (path.equals("") ? Paths.get(pathOfRegularFiles):Paths.get(pathOfConfidentialFiles + authentication.getName() +"\\" + path ));

    }

}

