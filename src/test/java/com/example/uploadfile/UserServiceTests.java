package com.example.uploadfile;

import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.service.UploadService;
import com.example.uploadfile.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    @Autowired
    private WhiteListUserRepository whiteListUserRepository;


    @Autowired
    UserService userService;



}
