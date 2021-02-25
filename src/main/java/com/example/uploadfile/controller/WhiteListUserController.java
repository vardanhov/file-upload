package com.example.uploadfile.controller;

import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.WhiteListUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/whitelist", produces = MediaType.APPLICATION_JSON_VALUE)
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


    @PostMapping("/create-white-list-by-username")
    public WhiteListUserDto createWhiteListUserByUserName(@RequestParam String username) {
      return   whiteListUserService.createWhiteListUserByUserName(username);
    }


    @ApiOperation(value = "update/{id}")
    @PutMapping("/updateWhiteListUser")
    public void updateWhiteListUser(@Valid @RequestBody WhiteListUserDto whiteListUserDto, @PathVariable(name = "id") Integer id) {
        whiteListUserService.updateWhiteList(whiteListUserDto,id);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "change access")
    @PutMapping("/change-access/{id}")
    public void changeAccess(@PathVariable Integer id, @RequestParam boolean accessType) {
        whiteListUserService.changeAccess(id,accessType);
    }
}
