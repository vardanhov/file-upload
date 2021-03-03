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

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.uploadfile.util.UserMapper.toWhiteListUserDto;


@Service
public class UploadServiceImpl implements UploadService {
    final static private Integer MIN_FILENAME_LENGTH = 4;

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    @Value("${allowed.file.content.types}")
    private String allowedContentTypes;

    private final WhiteListUserRepository whiteListUserRepository;

    @Autowired
    public UploadServiceImpl(WhiteListUserRepository whiteListUserRepository) {
        this.whiteListUserRepository = whiteListUserRepository;
    }

    //TODO надо пользователю как-то говорить какие были загружены а какие нет
    @Override
    public List<File> storeFiles(MultipartFile[] multipartFileList, String path, Authentication authentication) {
        checkUserUploadRights(authentication);

        return Stream.of(multipartFileList)
                .map(multipartFile -> storeFile(multipartFile, path, authentication))
                .collect(Collectors.toList());
    }

    //TODO еще поработать с этим методом
    private File storeFile(MultipartFile multipartFile, String path, Authentication authentication) {
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

        if (multipartFile == null) {
            throw new FileNotFoundException("Файл отсутствует");
        }

        Path pathSaveFileTo = returnTargetPath(path, authentication);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if (fileName.length() < MIN_FILENAME_LENGTH) {
            throw new FileNameException("Некорректное имя файла");
        }

        //TODO из настроек массив допустимых типов файлов
//        if (!fileName.endsWith(".py")) { throw new FileNameException("Неверный формат файла"); }

        File file = new File(pathSaveFileTo.toString(), multipartFile.getOriginalFilename());

        if (file.exists()) {
            throw new FileNameException("Файл с таким именем уже существует, выберите другое имя");
        } else if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new FileStorageException(String.format("Файл не может быть сохранен!"));
            }
        }
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Файл не был сохранен. Попробуйте еще раз!"));
        }
        return file;
        //Fixme
//        if (!Objects.equals(multipartFile.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
//            throw new FileContentTypeException("Invalid content type: " + multipartFile.getContentType());
//        }
    }


//Fixme
    @Override
    public void checkUserUploadRights(Authentication authentication) {
        WhiteListUserDto whiteListUserDto = null;
                try {
                    whiteListUserDto = toWhiteListUserDto(
                            whiteListUserRepository.getWhiteListUserByUserName(authentication.getName())
                    );
                } catch (Exception e) {
                    throw new RuntimeException(
                            "У Вас недостаточно прав для добавления файлов, обратитесь к администратору"
                    );
                }
        if (!isWithinRange(whiteListUserDto.getDateFrom(), whiteListUserDto.getDateTo())) {
            throw new RuntimeException("Не установлен, либо исчерпан разрешенный диапазон времени для загрузки файлов,"
                    + " обратитесь к администратору");
        }
    }
//TODO refactor
    public boolean checkUserUploadRightsOnEnter(Authentication authentication) {
        WhiteListUserDto whiteListUserDto = null;
        try {
            whiteListUserDto = toWhiteListUserDto(
                    whiteListUserRepository.getWhiteListUserByUserName(authentication.getName())
            );
        } catch (Exception e) {
            throw new RuntimeException("У Вас недостаточно прав для добавления файлов, обратитесь к администратору");
        }
        return isWithinRange(whiteListUserDto.getDateFrom(), whiteListUserDto.getDateTo());
    }

    private boolean isWithinRange(@NotNull LocalDateTime from, @NotNull LocalDateTime to) {
        if (from == null || to == null) {
            throw new RuntimeException("Не установлен, либо исчерпан допустимый диапазон"
                    + " времени для загрузки, обратитесь к администратору");
        }

        LocalDateTime currentTime = Instant.ofEpochMilli(System.currentTimeMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return from.isBefore(currentTime) && to.isAfter(currentTime);
    }

    private Path returnTargetPath(String path, Authentication authentication) {
        return (!path.equals("") ? Paths.get(pathOfConfidentialFiles + authentication.getName() + "\\" + path)
                : Paths.get(pathOfRegularFiles));
    }
}

