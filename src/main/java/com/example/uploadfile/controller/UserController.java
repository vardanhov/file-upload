package com.example.uploadfile.controller;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.dto.UserDto;
import com.example.uploadfile.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class UserController {


    @Autowired
    private UserService userService;



    @GetMapping("/")
    public String welcome() {
        return "index";
    }

//    @GetMapping("/hello")
//    public String hello(Model model, Principal principal) {
//        model.addAttribute("user", principal.getName());
//        return "hello";
//    }

    @GetMapping("/hello")
    public String hello(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getName());
        return "hello";
    }


    @ApiOperation(value = "get all users")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "create")
    @PostMapping("/createUser")
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }
}
