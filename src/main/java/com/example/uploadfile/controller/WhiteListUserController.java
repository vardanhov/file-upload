package com.example.uploadfile.controller;

import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.WhiteListUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WhiteListUserController {

    @Autowired
    private WhiteListUserService whiteListUserService;

//TODO - надо будет переписать метод, или написать другой.
// Мы с фронта не сможем юзера присылать, мы можем только логин,
// поэтому надо по логину искать пользователя в AD
    @ApiOperation(value = "create")
    @PostMapping("/createWhiteListUser")
    public void createWhiteListUser(@RequestBody WhiteListUserDto whiteListUserDto) {
        whiteListUserService.createOrUpdateWhiteList(whiteListUserDto);
    }

    @ApiOperation(value = "update/{id}")
    @PostMapping("/updateWhiteListUser")
    public void updateWhiteListUser(@RequestBody WhiteListUserDto whiteListUserDto, @PathVariable(name = "id") String id) {
        whiteListUserService.createOrUpdateWhiteList( whiteListUserDto);
    }

}
