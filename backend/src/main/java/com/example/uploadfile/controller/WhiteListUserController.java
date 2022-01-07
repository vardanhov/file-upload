package com.example.uploadfile.controller;

import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.iface.GroupService;
import com.example.uploadfile.service.iface.UserService;
import com.example.uploadfile.service.iface.WhiteListUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/whitelist", produces = MediaType.APPLICATION_JSON_VALUE)
public class WhiteListUserController {

    private final WhiteListUserService whiteListUserService;
    private final GroupService groupService;
    private final UserService userService;


    @Autowired
    public WhiteListUserController(WhiteListUserService whiteListUserService, GroupService groupService, UserService userService) {
        this.whiteListUserService = whiteListUserService;
        this.groupService = groupService;
        this.userService = userService;
    }


    @ApiOperation(value = "список пользователей")
    @GetMapping
    public ResponseEntity<List<WhiteListUserDto>> admin(Authentication authentication) {
        return ResponseEntity.ok(whiteListUserService.getAllUsers(authentication));
    }


    @ApiOperation(value = "create white list by User name")
    @PostMapping("/add-by-username")
    public WhiteListUserDto createWhiteListUserByUserName(@RequestParam String userName,
                                                          @RequestParam String groupName,
                                                          Authentication authentication) {
        return whiteListUserService.createWhiteListUserByUserName(userName, groupName, authentication);
    }

    @ApiOperation(value = "find by username")
    @PostMapping("/find-by-username")
    public void findByUsername(@RequestParam String userName) {
         whiteListUserService.findByUserName(userName);
    }

    //Fixme убрать заглушку и написать метод добавления из ад с назначением группы
    @ApiOperation(value = "create white list by User name with group")
    @PostMapping("/add-user")
    public void addWhiteListUserFromLdapByUserName(@RequestParam String userName,
                                                   @RequestParam String groupName,
                                                   Authentication authentication) {

    }

    //TODO ghj
    @ApiOperation(value = "delete white list by User name with group")
    @DeleteMapping("/{guid}")
    public void deleteWhiteListUserByUserName(@PathVariable Long guid,
                                              Authentication authentication) {
        whiteListUserService.deleteWhiteListUserByGuid(guid);
    }


    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "ограничение доступа")
    @PostMapping("/limit-access/{guid}")
    public void limitAccess(@PathVariable Long guid,
                            Authentication authentication) {
        whiteListUserService.limitAccessById(guid, authentication);
    }

    @ApiOperation(value = "предоставление доступа")
    @PostMapping("/grant-access/{guid}")
    public void grantAccessById(@RequestParam String dateFrom,
                                @RequestParam String timeFrom,
                                @RequestParam String dateTo,
                                @RequestParam String timeTo,
                                @PathVariable Long guid, Authentication authentication) {
        whiteListUserService.grantAccessById(dateFrom, timeFrom, dateTo, timeTo, guid, authentication);
    }

    @ApiOperation(value = "Проверить  пользователя")
    @PostMapping("/check-by-username")
    public void checkUserExistInGroup(@RequestParam String username) {
        whiteListUserService.checkUser(username);
    }

}
