package com.example.uploadfile.service;


import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.excepion.WhiteListUserException;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WhiteListUserService {

    @Autowired
    private WhiteListUserRepository whiteListUserRepository;




    @Scheduled(cron = "0 0 0/1 * * ?")
    public void updateUserAccess() {
        whiteListUserRepository.changePermissions();
    }


    public List<WhiteListUserDto> getAllWhiteListUsers() {
        List<WhiteListUser> whiteListUsers = whiteListUserRepository.findAll();
        List<WhiteListUserDto> whiteListUserDto = new ArrayList<>();
        for (WhiteListUser whiteListUser : whiteListUsers) {
            whiteListUserDto.add(UserMapper.convertWhiteListUserToDto(whiteListUser));
        }
        return whiteListUserDto;
    }


    public void createOrUpdateWhiteList(WhiteListUserDto whiteListUserDto) {
        WhiteListUser whiteListUser;
        if (whiteListUserDto.getId() != null) {
            whiteListUser = whiteListUserRepository.findById(whiteListUserDto.getId()).orElseThrow(() -> new WhiteListUserException("Can not find White List"));
        }
     //   User user = userRepository.findByUsername(whiteListUserDto.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        whiteListUser = UserMapper.convertWhiteListUserDtoToUser(whiteListUserDto, new User());
        whiteListUserRepository.save(whiteListUser);
    }

}
