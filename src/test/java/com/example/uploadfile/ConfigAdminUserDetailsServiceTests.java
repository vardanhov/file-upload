package com.example.uploadfile;


import com.example.uploadfile.controller.UploadController;
import com.example.uploadfile.domain.User;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.service.UploadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UploadController.class, UploadService.class })
@SpringBootTest
public class ConfigAdminUserDetailsServiceTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void chek(){
      User user=  userRepository.findUserByUsername("anton");
        List<User> =

    }


}
