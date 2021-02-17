package com.example.uploadfile.service;


import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class WhiteListUserService {

    @Autowired
    private WhiteListUserRepository whiteListUserRepository;


    public List<WhiteListUserDto> getAllWhiteListUsers() {
        List<WhiteListUser> whiteListUsers = whiteListUserRepository.findAll();
        List<WhiteListUserDto> whiteListUserDto = new ArrayList<>();
        for (WhiteListUser whiteListUser :whiteListUsers){
            whiteListUserDto.add(UserMapper.convertWhiteListUserToDto(whiteListUser));
        }
        return whiteListUserDto;
    }



}
