package com.example.uploadfile.controller;

import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.WhiteListUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class WhiteListUserController {


    private WhiteListUserService whiteListUserService;

    @Autowired
    public WhiteListUserController(WhiteListUserService whiteListUserService) {
        this.whiteListUserService = whiteListUserService;
    }


    @ApiOperation(value = "create")
    @PostMapping("/createWhiteListUser")
    public void createWhiteListUser(@RequestBody WhiteListUserDto whiteListUserDto) {
        whiteListUserService.createWhiteList(whiteListUserDto);
    }

    @ApiOperation(value = "create white list by User name")
    @PostMapping("/createWhiteListUserByUserName")
    public WhiteListUserDto createWhiteListUserByUserName(@RequestParam String username) {
      return   whiteListUserService.createWhiteListUserByUserName(username);
    }



    @ApiOperation(value = "update/{id}")
    @PutMapping("/updateWhiteListUser")
    public void updateWhiteListUser(@Valid @RequestBody WhiteListUserDto whiteListUserDto, @PathVariable(name = "id") Integer id) {
        whiteListUserService.updateWhiteList(whiteListUserDto,id);
    }

}
