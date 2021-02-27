package com.example.uploadfile.service;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.repo.WhiteListUserRepository;
import com.example.uploadfile.service.iface.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public  User getUserByUserName(String username){
       return userRepository.findUserByUsername(username);
    }
}
