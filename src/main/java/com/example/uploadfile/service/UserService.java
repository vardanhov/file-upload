package com.example.uploadfile.service;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.repo.WhiteListUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    //Fixme Autowired лучше через конструктор, а то можно NPE словить
    @Autowired
    private WhiteListUserRepository whiteListUserRepository;

    //Fixme нам на админке надо получать всех вайтлист или лдаповских нужной группы?
    public List<WhiteListUser> getAllUsers() {
        return whiteListUserRepository.findAll();
    }

    //Fixme зачем нам создавать вайтюзеров? Разве это будут не лдаповские, которым мы даем права?
    public void createUser(WhiteListUser whiteListUser) {
        whiteListUserRepository.save(whiteListUser);
    }

    public void limitAccessById(Integer guid) {
        //Fixme переписать метод дизейблить по гуиду
//        WhiteListUser whiteListUser1 = new WhiteListUser();
//        whiteListUser1.setUpload(false);
//        whiteListUserRepository.save(whiteListUser1);
    }
    public void grantAccessById(String dateTime, Integer guid) {
        //TODO написать метод предоставления прав по времени
    }

}
