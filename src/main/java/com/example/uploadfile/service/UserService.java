package com.example.uploadfile.service;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.dto.UserDto;
import com.example.uploadfile.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


     public List<User> getAllUsers() {
        return userRepository.findAll();

    }

    public void createUser(User user){
         userRepository.save(user);
    }
}
