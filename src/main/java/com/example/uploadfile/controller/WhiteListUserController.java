package com.example.uploadfile.controller;

import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.WhiteListUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/whitelist", produces = MediaType.APPLICATION_JSON_VALUE)
public class WhiteListUserController {


    private WhiteListUserService whiteListUserService;

    @Autowired
    public WhiteListUserController(WhiteListUserService whiteListUserService) {
        this.whiteListUserService = whiteListUserService;
    }

    @ApiOperation(value = "список пользователей")
    @GetMapping
    public ResponseEntity<List<WhiteListUserDto>> admin(Authentication authentication) { return ResponseEntity.ok(whiteListUserService.getAllUsers(authentication)); }


    @ApiOperation(value = "create white list by User name")
    @PostMapping("/add-by-username")
    public WhiteListUserDto createWhiteListUserByUserName(@RequestBody String userName, Authentication authentication) {
        return whiteListUserService.createWhiteListUserByUserName(userName, authentication);
    }

//TODO добавить метод для добавления в вайт лист целой группы из АД


    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "ограничение доступа")
    @PostMapping("/limit-access/{guid}")
    public void limitAccess(@PathVariable Integer guid, Authentication authentication) {
        whiteListUserService.limitAccessById(guid, authentication);
    }

    @ApiOperation(value = "предоставление доступа")
    @PostMapping("/grant-access/{guid}")
    public void grantAccessById(@RequestBody String dateTimeFrom, @RequestBody String dateTimeTo, @PathVariable Integer guid, Authentication authentication) {
        whiteListUserService.grantAccessById(dateTimeFrom, dateTimeTo, guid, authentication);
    }

}
