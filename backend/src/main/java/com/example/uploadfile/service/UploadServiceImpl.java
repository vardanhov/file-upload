package com.example.uploadfile.service;

import com.example.uploadfile.domain.FileOwner;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.FileNameException;
import com.example.uploadfile.excepion.FileOwnerException;
import com.example.uploadfile.excepion.FileStorageException;
import com.example.uploadfile.logger.Markers;
import com.example.uploadfile.repo.AccessUserListEntityRepository;
import com.example.uploadfile.repo.FileOwnerRepository;
import com.example.uploadfile.repo.ParticipantEntityRepository;
import com.example.uploadfile.repo.UserGroupsEntityRepository;
import com.example.uploadfile.service.iface.GroupService;
import com.example.uploadfile.service.iface.UploadService;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.uploadfile.util.UserMapper.toWhiteListUserDto;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    //TODO вынести в проперти
    final static private Integer MIN_FILENAME_LENGTH = 4;

    @Value("${upload.path.regular}")
    private String pathOfRegularFiles;

    @Value("${upload.path.confidential}")
    private String pathOfConfidentialFiles;

    @Value("${upload.path.delimiter}")
    private String delimiter;

    @Value("${upload.path.regular}")
    private String defaultUploadPathDags;

    private final AccessUserListEntityRepository whiteListUserRepository;
    private final FileOwnerRepository fileOwnerRepository;
    private final GroupService groupService;
    private final ParticipantEntityRepository participantEntityRepository;
    private final UserGroupsEntityRepository userGroupsEntityRepository;

    @Autowired
    public UploadServiceImpl(AccessUserListEntityRepository whiteListUserRepository,
                             FileOwnerRepository fileOwnerRepository,
                             GroupService groupService,
                             ParticipantEntityRepository participantEntityRepository,
                             UserGroupsEntityRepository userGroupsEntityRepository) {
        this.whiteListUserRepository = whiteListUserRepository;
        this.fileOwnerRepository = fileOwnerRepository;
        this.groupService = groupService;
        this.participantEntityRepository = participantEntityRepository;
        this.userGroupsEntityRepository = userGroupsEntityRepository;
    }


    @Override
    public List<File> storeFiles(MultipartFile[] multipartFileList, Boolean isDag, String folder, Boolean canOverride, Authentication authentication) {
        return Stream.of(multipartFileList)
                .map(multipartFile -> storeFile(multipartFile, isDag, folder, canOverride, authentication))
                .collect(Collectors.toList());
    }

    private File storeFile(MultipartFile multipartFile, Boolean isDag, String folder, Boolean toOverride, Authentication authentication) {
        //проверяем, что для дага не прилетела кастомная папка
        folder = isDag ? "" : folder;
        String pathToString = defineTargetPath(folder, isDag, authentication);
        Path path = Paths.get(pathToString);
        boolean isFolderNew = !path.toFile().exists();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if (!("".equals(folder) || path.toFile().exists() && isOwner(path, authentication) || !path.toFile().exists())) {
            log.warn(Markers.USER.getMarker(), "[Неверная директория] | username={} | message={} | type={}",
                    authentication.getName(),
                    "Попытка записи файла  в чужую директорию | " + fileName + " | " + pathToString + " | " + "isDag="
                            + isDag + " | " + isFolderNew, "UserEvent");
            throw new RuntimeException(String.format("Файл не был загружен. Нет доступа к указанной папке %s", folder)
            );
        }

        if (!checkFileType(fileName, isDag)) {
            log.warn(Markers.USER.getMarker(), "[Неверный тип] | username={} | message={} | type={}",
                    authentication.getName(),
                    "Попытка записи файла в неверном формате для дага | " + fileName + " | " + pathToString + " | " + "isDag="
                            + isDag + " | " + isFolderNew, "UserEvent");
            throw new FileNameException("Неверный формат для дага");
        }

        if (!checkFileName(fileName)) {
            log.warn(Markers.USER.getMarker(), "[Некорректное имя файла] | username={} | message={} | type={}",
                    authentication.getName(),
                    "Попытка записи файла с некорректным именем | " + fileName + " | " + pathToString + " | " + "isDag="
                            + isDag + " | " + isFolderNew, "UserEvent");
            throw new FileNameException("Некорректное имя файла");
        }

        File file = new File(pathToString, multipartFile.getOriginalFilename());

        if (toOverride && !isOwner(path, authentication)
                && !(path.toString().equals(defaultUploadPathDags) || path.toString().equals(pathOfConfidentialFiles))
                && !((pathOfConfidentialFiles + participantEntityRepository.findParticipantEntityByUsername(
                authentication.getName()).map(el -> el.getUserGroups().getGroupName()).orElse("")).equals(path.toString()))) {
            log.warn(Markers.USER.getMarker(), "[Перезапись файла] | username={} | message={} | type={}",
                    authentication.getName(),
                    "Попытка записи в чужую директорию | " + fileName + " | " + pathToString + " | " + "isDag=" + isDag + " | " + isFolderNew, "UserEvent");
        }
        if (toOverride && !isOwner(file.toPath(), authentication)) {
            log.warn(Markers.USER.getMarker(), "[Перезапись файла] | username={} | message={} | type={}",
                    authentication.getName(),
                    "Попытка перезаписи чужого файла | " + fileName + " | " + pathToString + " | " + "isDag=" + isDag + " | " + isFolderNew, "UserEvent");
        }

        if (!file.exists() || file.exists() && toOverride) {
            try {
                if (!file.exists()) {
                    log.warn(Markers.USER.getMarker(), "[Сохранение нового файла] | username={} | message={} | type={}",
                            authentication.getName(),
                            "Запись нового файла | " + fileName + " | " + pathToString + " | " + "isDag=" + isDag + " | " + isFolderNew, "UserEvent");
                }

                if (file.exists() && toOverride) {
                    log.warn(Markers.USER.getMarker(), "[Перезапись файла] | username={} | message={} | type={}",
                            authentication.getName(),
                            "Перезапись существующего файла | " + fileName + " | " + pathToString + " | " + "isDag="
                                    + isDag + " | " + isFolderNew, "UserEvent");
                }

                file.getParentFile().mkdirs();
                file.createNewFile();
                multipartFile.transferTo(file);
            } catch (IOException e) {
                log.warn(Markers.USER.getMarker(), "[Сохранение файла] | username={} | message={} | type={}",
                        authentication.getName(),
                        "Неуспешная попытка записи файла | " + fileName + " | " + pathToString + " | " + "isDag=" + isDag + " | " + isFolderNew, "UserEvent");
                throw new FileStorageException(String.format("Файл не может быть сохранен!"));
            }
        }

        //TODO воткнуть лог (подумать, чтобы не выводить в хистори пользователю эту инфу)
        try {
            setOwner(file.toPath(), authentication.getName());
        } catch (IOException e) {
            //TODO воткнуть лог
            throw new FileOwnerException(String.format("Невозможно назначить пользователя %s владельцем файла %s", authentication.getName(), file));
        }


        if (!isDag && !"".equals(folder) && path.toFile().exists()) {
            try {
                setOwner(file.getParentFile().toPath(), authentication.getName());
                //TODO воткнуть лог
            } catch (IOException e) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException exception) {
                    //TODO воткнуть лог
                    throw new RuntimeException(String.format("Невозможно удалить файл", authentication.getName(), folder));
                }
                //TODO воткнуть лог
                throw new FileOwnerException(String.format("Невозможно назначить пользователя %s владельцем директории %s",
                        authentication.getName(), folder));
            }
        }

        return file;
    }


    @Override
    public Map<String, Boolean> fileAndFolderChecks(String fileName, Boolean isDag, String folder,
                                                    Authentication authentication) {
        String pathUri = defineTargetPath(folder, isDag, authentication);
        Path path = Paths.get(pathUri);
        File file = new File(String.format("%s%s%s", pathUri, delimiter, fileName));
        Boolean isFolderExists = path.toFile().exists();
        Boolean isFileExists = file.exists();
        Boolean isFolderOwner = "".equals(folder) || isOwner(path, authentication);
        Map<String, Boolean> resultChecks = new HashMap<>();

        resultChecks.put("folderExists", isFolderExists);
        resultChecks.put("fileExists", isFileExists);
        resultChecks.put("isFolderOwner", "".equals(folder) || (isFolderExists && isFolderOwner) || !isFolderExists);
        resultChecks.put("isFileOwner", (isFolderExists && isFileExists && isFolderOwner && isOwner(file.toPath(), authentication))
                || (isFolderExists && isFolderOwner && !isFileExists));

        return resultChecks;
    }

    private Boolean checkFileName(String fileName) {
        return !fileName.isEmpty();
    }

    private Boolean checkFileType(String fileName, Boolean isDag) {
        if (isDag) {
            return fileName.endsWith(".py");
        } else {
            return true;
        }
    }

    //Fixme
    @Override
    public void checkUserUploadRights(Authentication authentication) {
        WhiteListUserDto whiteListUserDto;
        try {
            whiteListUserDto = toWhiteListUserDto(
                    whiteListUserRepository.getWhiteListUserByUserName(authentication.getName()));
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
    public Boolean checkUserUploadRightsOnEnter(Authentication authentication) {
        WhiteListUserDto whiteListUserDto;
        try {
            whiteListUserDto = toWhiteListUserDto(
                    whiteListUserRepository.getWhiteListUserByUserName(authentication.getName()));
        } catch (Exception e) {
            throw new RuntimeException("У Вас недостаточно прав для добавления файлов, обратитесь к администратору");
        }
        return isWithinRange(whiteListUserDto.getDateFrom(), whiteListUserDto.getDateTo());
    }

    //Fixme - не забыть удалить после того, как доделаем шедулер
    private boolean isWithinRange(@NotNull LocalDateTime from, @NotNull LocalDateTime to) {
        LocalDateTime currentTime = Instant.ofEpochMilli(System.currentTimeMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return (from != null && to != null) && from.isBefore(currentTime) && to.isAfter(currentTime);
    }

    private String defineTargetPath(String folder, Boolean isDag, Authentication authentication) {
        String pathToGroupFolder = pathOfConfidentialFiles + participantEntityRepository.
                findParticipantEntityByUsername(authentication.getName())
                .get().getUserGroups().getGroupName();
        return (isDag ? pathOfRegularFiles
                : "".equals(folder) ? pathToGroupFolder : pathToGroupFolder + delimiter + folder);
    }


    private void setOwner(Path path, String name) throws IOException {
        //TODO воткнуть лог
        FileOwner f = fileOwnerRepository.findFileOwnerByPath(path.toString());

        if (f == null) {
            FileOwner fileOwner = new FileOwner();
            fileOwner.setUsername(name);
            fileOwner.setPath(path.toString());
            fileOwnerRepository.save(fileOwner);
        } else {
            f.setUsername(name);
            fileOwnerRepository.save(f);
        }
    }

    private Boolean isOwner(Path targetPath, Authentication authentication) {
        //TODO воткнуть лог
        FileOwner fileOwner = fileOwnerRepository.findFileOwnerByPath(targetPath.toString());
        return fileOwner != null && fileOwner.getUsername().equals(authentication.getName());
    }
}

