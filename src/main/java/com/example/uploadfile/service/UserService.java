package com.example.uploadfile.service;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.repo.WhiteListUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private WhiteListUserRepository whiteListUserRepository;


     public List<WhiteListUser> getAllUsers() {
        return whiteListUserRepository.findAll();

    }

    public void createUser(WhiteListUser whiteListUser){
        whiteListUserRepository.save(whiteListUser);
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void checkAccess(){
         Long currentDateTimeMillisecond =System.currentTimeMillis();
        whiteListUserRepository.changePermissions(currentDateTimeMillisecond);


    }

    public void limitAccess(WhiteListUser whiteListUser){
        WhiteListUser whiteListUser1 = new WhiteListUser();
        whiteListUser1.setUpload(false);
        whiteListUserRepository.save(whiteListUser1);
    }
}
