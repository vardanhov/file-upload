package com.example.uploadfile.service;


import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.repo.WhiteListUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhiteListUserService {

    @Autowired
    private WhiteListUserRepository whiteListUserRepository;

  public   List<WhiteListUser> getAllWhiteListUsers(){

        return whiteListUserRepository.findAll();

    }


}
