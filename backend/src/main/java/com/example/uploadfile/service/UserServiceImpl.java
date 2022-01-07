package com.example.uploadfile.service;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.excepion.InvalidOldPasswordException;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.repo.UserRepository;
import com.example.uploadfile.service.iface.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByUserName(String username) {
User user = userRepository.findUserByUsername(username);
if(user==null){
    throw new UserNotFoundException("Такого пользователя нет");
}
        return user;
    }

    @Override
    public void updatePassword(String password, String oldPassword, String name) {
        User user = userRepository.findUserByUsername(name);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidOldPasswordException();
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
