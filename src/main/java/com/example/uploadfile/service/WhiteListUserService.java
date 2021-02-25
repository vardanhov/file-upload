package com.example.uploadfile.service;


import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.excepion.WhiteListUserException;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WhiteListUserService {


    private UserRepository userRepository;


    private WhiteListUserRepository whiteListUserRepository;

    @Autowired
    public WhiteListUserService(UserRepository userRepository, WhiteListUserRepository whiteListUserRepository) {
        this.userRepository = userRepository;
        this.whiteListUserRepository = whiteListUserRepository;
    }

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


    @Transactional
    public void createWhiteList(WhiteListUserDto whiteListUserDto) {
        User user = userRepository.findUserByUsername(whiteListUserDto.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        WhiteListUser whiteListUser = UserMapper.convertWhiteListUserDtoToUser(whiteListUserDto, user);
        whiteListUserRepository.save(whiteListUser);
    }



    @Transactional
    public void updateWhiteList(WhiteListUserDto whiteListUserDto, Integer id) {
        WhiteListUser whiteListUser = whiteListUserRepository.findById(id).orElseThrow(() -> new WhiteListUserException("Can not find White List"));
        User user = userRepository.findUserByUsername(whiteListUserDto.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        whiteListUser = UserMapper.convertWhiteListUserDtoToUser(whiteListUserDto, user);
        whiteListUserRepository.save(whiteListUser);
    }



    @Transactional
    public WhiteListUserDto createWhiteListUserByUserName(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user==null){
            throw new UserNotFoundException("Can not find user");
        }
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setUsername(user.getUsername());
        WhiteListUser whiteListUserResponse = whiteListUserRepository.saveAndFlush(whiteListUser);
        WhiteListUserDto whiteListUserDto = UserMapper.convertWhiteListUserToDto(whiteListUserResponse);
        return whiteListUserDto;
    }

}
