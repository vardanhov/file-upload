package com.example.uploadfile.util;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;

public class UserMapper {

    public static WhiteListUserDto toWhiteListUserDto(WhiteListUser whiteListUser) {
        WhiteListUserDto whiteListUserDto = new WhiteListUserDto();
        whiteListUserDto.setId(whiteListUser.getId());
        whiteListUserDto.setUserName(whiteListUser.getUserName());
        whiteListUserDto.setFullName(whiteListUser.getFullName());
        whiteListUserDto.setGroup(whiteListUser.getGroup());
        whiteListUserDto.setCreateDate(whiteListUser.getCreateDate());
        whiteListUserDto.setDateFrom(whiteListUser.getDateFrom());
        whiteListUserDto.setDateTo(whiteListUser.getDateTo());
        whiteListUserDto.setAdmin(whiteListUser.getAdmin());
        return whiteListUserDto;
    }

    public static WhiteListUser toWhiteListUser(WhiteListUserDto whiteListUserDto) {
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setUserName(whiteListUserDto.getUserName());
        whiteListUser.setFullName(whiteListUserDto.getFullName());
        whiteListUser.setGroup(whiteListUserDto.getGroup());
        whiteListUser.setCreateDate(whiteListUserDto.getCreateDate());
        whiteListUser.setDateFrom(whiteListUserDto.getDateFrom());
        whiteListUser.setDateTo(whiteListUserDto.getDateTo());
        whiteListUser.setAdmin(whiteListUserDto.getAdmin());
        return whiteListUser;
    }
}
