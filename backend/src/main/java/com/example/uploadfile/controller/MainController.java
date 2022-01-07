package com.example.uploadfile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"", "/", "/upload", "/admin", "/home", "/account", "/audit", "/login"})
public class MainController {

    @GetMapping
    public String main() {
        return "index";
    }
}
