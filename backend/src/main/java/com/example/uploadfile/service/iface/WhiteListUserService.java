package com.example.uploadfile.service.iface;

import com.example.uploadfile.dto.WhiteListUserDto;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface WhiteListUserService {
    WhiteListUserDto createWhiteListUserByUserName(String username, String groupName, Authentication authentication);
//    WhiteListUserDto createWhiteListUserByUserNameWithGroup(String username, String groupName, Authentication authentication);
    List<WhiteListUserDto> getAllUsers(Authentication authentication);
    void grantAccessById(String dateFrom,
                         String timeFrom,
                         String dateTo,
                         String timeTo,
                         Long guid,
                         Authentication authentication);
    void limitAccessById(Long guid, Authentication authentication);
    void updateExpiredUser();
    void deleteWhiteListUserByGuid(Long guid);
    void findByUserName(String username);
    void checkUser(String username);
}
