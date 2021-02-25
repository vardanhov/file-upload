package com.example.uploadfile.service;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

    private WhiteListUserRepository whiteListUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, WhiteListUserRepository whiteListUserRepository) {
        this.userRepository = userRepository;
        this.whiteListUserRepository = whiteListUserRepository;
    }

    //Fixme нам на админке надо получать всех вайтлист или лдаповских нужной группы?
    public List<WhiteListUser> getAllUsers() {
        return whiteListUserRepository.findAll();
    }

    //Fixme зачем нам создавать вайтюзеров? Разве это будут не лдаповские, которым мы даем права?
    public void createUser(WhiteListUser whiteListUser) {
        whiteListUserRepository.save(whiteListUser);
    }


    public void grantAccessById(String dateTime, Integer guid) {
        //TODO написать метод предоставления прав по времени
    }

    public  User getUserByUserName(String username){
       return userRepository.findUserByUsername(username);

    }
}
