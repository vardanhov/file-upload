package com.example.uploadfile.controller;

import com.example.uploadfile.domain.Authorities;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.domain.enums.UserProfile;
import com.example.uploadfile.ldap.LdapUser;
import com.example.uploadfile.service.UserService;
import com.sun.security.auth.LdapPrincipal;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private LdapTemplate ldapTemplate;


    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> sds = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
        String role = sds.get(0);
        if ("ROLE_USER".equals(role)) {
            return "hello";
        } else {
            return "admin";
        }
    }
//    @GetMapping("/hello")
//    public String hello(Model model, Authentication authentication) {
//        model.addAttribute("user", authentication.getName());
//        return "hello";
//    }


        @ApiOperation(value = "get all users")
        @GetMapping("/users")
        public List<WhiteListUser> getAllUsers () {
            return userService.getAllUsers();
        }

        @ApiOperation(value = "create")
        @PostMapping("/createUser")
        public void createUser (@RequestBody WhiteListUser whiteListUser){

            userService.createUser(whiteListUser);
        }

        @ApiOperation(value = "ограничить доступ")
        @PostMapping("/limitAccess")
        public void limitAccess (@RequestBody WhiteListUser whiteListUser){
            userService.limitAccess(whiteListUser);
        }

//    @GetMapping("/login")
//    public ModelAndView login() {
//        ArrayList<?> membersOf = ldapTemplate.search(
//                query().where("sAMAccountName").is(userCN),
//                (AttributesMapper<ArrayList<?>>) attrs -> Collections.list(attrs.get("memberOf").getAll())
//        ).get(0);
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("custom-login");
//
//
//        return mav;
//    }
    }
