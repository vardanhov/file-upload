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
    @PostMapping("/create-white-list-by-username")
    public WhiteListUserDto createWhiteListUserByUserName(@RequestParam String username) {
      return   whiteListUserService.createWhiteListUserByUserName(username);
    }


    @ApiOperation(value = "update whit list")
    @PutMapping("/update-white-list/{id}")
    public void updateWhiteListUser(@Valid @RequestBody WhiteListUserDto whiteListUserDto, @PathVariable(name = "id") Integer id) {
        whiteListUserService.updateWhiteList(whiteListUserDto,id);
    }

    @ApiOperation(value = "change access")
    @PutMapping("/change-access/{id}")
    public void changeAccess(@PathVariable Integer id, @RequestParam boolean accessType) {
        whiteListUserService.changeAccess(id,accessType);
    }
}
