package com.example.uploadfile.controller;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.service.iface.UserService;
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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @ApiOperation(value = "найти пользователя")
    @PostMapping("/find-by-username")
    public ResponseEntity<User> findUserByUsername(@RequestBody String username, Authentication authentication) {
      return ResponseEntity.ok(userService.getUserByUserName(username));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}