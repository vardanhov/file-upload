package com.example.uploadfile.service;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;
import com.example.uploadfile.repo.WhiteListUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private WhiteListUserRepository whiteListUserRepository;


    public List<WhiteListUser> getAllUsers() {
        return whiteListUserRepository.findAll();
    }

    public void createUser(WhiteListUser whiteListUser) {
        whiteListUserRepository.save(whiteListUser);
    }

    public void update(WhiteListUser whiteListUser) {
        whiteListUserRepository.save(whiteListUser);
    }

    public void delete(Integer guid) {
        whiteListUserRepository.deleteById(guid);
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void checkPermissions() {
        // userRepository.changePermissions();
    }

    public void limitAccess(WhiteListUser whiteListUser) {
        WhiteListUser whiteListUser1 = new WhiteListUser();
        whiteListUser1.setUpload(false);
        whiteListUserRepository.save(whiteListUser1);
    }

}
