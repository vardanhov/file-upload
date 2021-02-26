package com.example.uploadfile.service.iface;

import com.example.uploadfile.domain.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    void limitAccessById(Integer guid, Authentication authentication);
    void grantAccessById(String dateTimeFrom, String dateTimeTo, Integer guid, Authentication authentication);
    User getUserByUserName(String username);
}
