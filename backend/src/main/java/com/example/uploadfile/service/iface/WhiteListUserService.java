package com.example.uploadfile.service.iface;

import com.example.uploadfile.dto.WhiteListUserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface WhiteListUserService {
    WhiteListUserDto createWhiteListUserByUserName(String username, Authentication authentication);
    List<WhiteListUserDto> getAllUsers(Authentication authentication);
    void grantAccessById(String dateFrom, String timeFrom, String dateTo, String timeTo, Integer guid, Authentication authentication);
    void limitAccessById(Integer guid, Authentication authentication);
    void checkUserAdminRights(Authentication authentication);
}
