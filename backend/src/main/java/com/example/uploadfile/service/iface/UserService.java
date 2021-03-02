package com.example.uploadfile.service.iface;

import com.example.uploadfile.domain.User;

public interface UserService {
    User getUserByUserName(String username);
}
