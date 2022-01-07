package com.example.uploadfile.controller;

import com.example.uploadfile.service.iface.GroupService;
import com.example.uploadfile.service.iface.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    public GroupController(UserService userService,
                           GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "Получить список групп")
    @GetMapping
    public ResponseEntity<?> getGroups(Authentication authentication) {
        try {
            return ResponseEntity.ok(groupService.getGroups());
        } catch (RuntimeException exception) {
            //TODO Добавить ответ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @ApiOperation(value = "Создать группу")
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestParam String groupName) {
        groupService.createGroupEntity(groupName);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @ApiOperation(value = "Удалить группу")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

}
