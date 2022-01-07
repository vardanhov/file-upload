package com.example.uploadfile.controller;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.excepion.InvalidOldPasswordException;
import com.example.uploadfile.service.iface.GroupService;
import com.example.uploadfile.service.iface.LoggingEventService;
import com.example.uploadfile.service.iface.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final LoggingEventService eventService;
    private final GroupService groupService;
    private final String defaultPageNumber = "0";
    private final String defaultPageSize = "100";

    public UserController(UserService userService, LoggingEventService eventService, GroupService groupService) {
        this.userService = userService;
        this.eventService = eventService;
        this.groupService = groupService;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "найти пользователя")
    @PostMapping("/find-by-username")
    public ResponseEntity<User> findUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUserName(username));
    }


    @ApiOperation(value = "Получить историю пользователя")
    @GetMapping("/history")
    public ResponseEntity<?> getUsrHistory(@RequestParam(defaultValue = defaultPageNumber) int page,
                                           @RequestParam(defaultValue = defaultPageSize) int size,
                                           Authentication authentication) {

        Pageable paging = PageRequest.of(page, size, Sort.by("timestmp").descending());

        try {
            return ResponseEntity.ok(eventService.getUserActionByUserName(authentication.getName(), paging));
        } catch (RuntimeException exception) {
            //TODO Добавить ответ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @ApiOperation(value = "Получить историю всех пользователей")
    @GetMapping("/all-histories")
    public ResponseEntity<?> getAllUserHistory(@RequestParam(defaultValue = defaultPageNumber) int page,
                                               @RequestParam(defaultValue = defaultPageSize) int size,
                                               Authentication authentication) {

        Pageable paging = PageRequest.of(page, size, Sort.by("timestmp").descending());

        try {
            return ResponseEntity.ok(eventService.getAllEvent(paging));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }


    @ApiOperation(value = "Добавить пользователей в группу")
    @PostMapping("/add-into-group")
    public ResponseEntity<?> addUserIntoGroup(@RequestParam Set<String> users, @RequestParam String groupName) {
        groupService.addUserIntoGroup(users, groupName);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @ApiOperation(value = "Удалить пользователя из группы")
    @DeleteMapping("/delete-from-group")
    public ResponseEntity<?> deleteUserFromGroup(@RequestParam String username) {
        groupService.deleteUserFromGroup(username);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @ApiOperation(value = "Смена пароля пользователя")
    @PostMapping("/user/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestParam("password") String password,
                                                 @RequestParam("oldpassword") String oldPassword,
                                                 Authentication authentication) {
        try {
            userService.updatePassword(password, oldPassword, authentication.getName());
            return ResponseEntity.ok("Success");
        } catch (InvalidOldPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @ApiOperation(value = "Получить логи для аудитора")
    @GetMapping("/get-audit-logs")
    public ResponseEntity<ByteArrayResource> getAuditLogs() throws IOException {

        File file = eventService.createFileAllEvents();

        Path path = Paths.get(file.getPath());
        byte[] data = Files.readAllBytes(path);
        file.delete();

        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(resource);
    }

    @ApiOperation(value = "Получить логи для пользователя")
    @GetMapping("/get-user-logs")
    public ResponseEntity<ByteArrayResource> getUserLogs(Authentication authentication) throws IOException {

        File file = eventService.createFileUseEvents(authentication.getName());

        Path path = Paths.get(file.getPath());
        byte[] data = Files.readAllBytes(path);
        file.delete();

        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(resource);
    }
}

