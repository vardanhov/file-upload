package com.example.uploadfile.controller;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<WhiteListUser>> admin() {
        return
                ResponseEntity.ok(userService.getAllUsers());
    }

    @ApiOperation(value = "ограничить доступ")
    @PostMapping("/limit-access/{guid}")
    public void limitAccess(@PathVariable Integer guid) {
        userService.limitAccessById(guid);
    }

    @ApiOperation(value = "update")
    @PostMapping("/grant-access/{guid}")
    public void grantAccessById(@RequestBody String dateTime, @PathVariable Integer guid) {
        userService.grantAccessById(dateTime, guid);
    }
}
