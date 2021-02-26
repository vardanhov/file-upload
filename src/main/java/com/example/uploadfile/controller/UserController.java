package com.example.uploadfile.controller;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.service.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @ApiOperation(value = "ограничение доступа")
    @PostMapping("/limit-access/{guid}")
    public void limitAccess(@PathVariable Integer guid, Authentication authentication) {
        userServiceImpl.limitAccessById(guid, authentication);
    }

    @ApiOperation(value = "предоставление доступа")
    @PostMapping("/grant-access/{guid}")
    public void grantAccessById(@RequestParam String dateTime, @PathVariable Integer guid, Authentication authentication) {
//        userService.grantAccessById(dateTime, guid);
    }

    @ApiOperation(value = "найти пользователя")
    @PostMapping("/find-by-username")
    public ResponseEntity<User> findUserByUsername(@RequestBody String username, Authentication authentication) {
      return ResponseEntity.ok(userServiceImpl.getUserByUserName(username));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}