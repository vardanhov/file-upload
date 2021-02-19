package com.example.uploadfile.controller;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.service.UserService;
import com.example.uploadfile.service.WhiteListUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private WhiteListUserService whiteListUserService;


    @GetMapping("/")
    public String welcome() {
        return "login";
    }

    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> sds = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
        String role = sds.get(0);
        if ("ROLE_USER".equals(role)) {
            model.addAttribute("user", principal.getName());
            return "upload";
        } else {
            List<WhiteListUserDto> whiteListUsers = whiteListUserService.getAllWhiteListUsers();
            model.addAttribute("whitelist", whiteListUsers);
            return "upload";
        }
    }

    @GetMapping("/adminPanel")
    public String admin(Model model) {
        List<WhiteListUserDto> whiteListUsers = whiteListUserService.getAllWhiteListUsers();
        model.addAttribute("whitelist", whiteListUsers);
        return "admin";
    }


//    @ApiOperation(value = "get all users")
//    @GetMapping("/users")
//
//    public List<WhiteListUser> getAllUsers() {
//        return userService.getAllUsers();
//    }

//    @ApiOperation(value = "create")
//    @PostMapping("/create")
//    public void createUser(@RequestBody WhiteListUser whiteListUser) {
//        userService.createUser(whiteListUser);
//    }

//    @ApiOperation(value = "create")
//    @GetMapping("/create")
//    public String createUser(Model model) {
////        userService.createUser(whiteListUser);
//        return "newUser";
//    }
//
//    @ApiOperation(value = "update")
//    @GetMapping("/update")
//    public String update() {
////        userService.update(whiteListUser);
//        return "update";
//
//    }
//TODO
    @ApiOperation(value = "delete")
    @PostMapping("/delete/{guid}")
    public String delete(@PathVariable Integer guid, Model model) {
        userService.delete(guid);
        List<WhiteListUserDto> whiteListUsers = whiteListUserService.getAllWhiteListUsers();
        model.addAttribute("whitelist", whiteListUsers);
        return "admin";

    }

    @ApiOperation(value = "ограничить доступ")
    @PostMapping("/limitAccess")
    public void limitAccess(@RequestBody WhiteListUser whiteListUser) {
        userService.limitAccess(whiteListUser);
    }

//    @GetMapping("/login")
//    public ModelAndView login() {
//    ldapTemplate.search(
//    query().where("objectclass").is("group"),
//    (AttributesMapper<String>) attributes -> attributes.get("cn").get().toString();
//);
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("custom-login");
//
//
//        return mav;
//    }
}
