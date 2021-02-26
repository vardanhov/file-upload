package com.example.uploadfile.service;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void limitAccessById(Integer guid, Authentication authentication) {
        //Fixme переписать метод дизейблить по гуиду
//        WhiteListUser whiteListUser1 = new WhiteListUser();
//        whiteListUser1.setUpload(false);
//        whiteListUserRepository.save(whiteListUser1);
    }
    public void grantAccessById(String dateTimeFrom, String dateTimeTo, Integer guid, Authentication authentication) {
        //TODO написать метод предоставления прав по времени
    }

    public  User getUserByUserName(String username){
       return userRepository.findUserByUsername(username);

    }
}
