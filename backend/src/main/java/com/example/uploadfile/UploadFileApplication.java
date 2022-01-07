package com.example.uploadfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UploadFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadFileApplication.class, args);
    }
}
