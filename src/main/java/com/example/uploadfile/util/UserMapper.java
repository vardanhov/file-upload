package com.example.uploadfile.util;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserMapper {

    public static WhiteListUserDto convertWhiteListUserToDto(WhiteListUser whiteListUser) {
        WhiteListUserDto whiteListUserDto = new WhiteListUserDto();
        whiteListUserDto.setId(whiteListUser.getId());
        whiteListUserDto.setUsername(whiteListUser.getUser().getUsername());
        whiteListUserDto.setCreateDate(formatDate(whiteListUser));
        whiteListUserDto.setTrigger(formatDate(whiteListUser));
        whiteListUserDto.setAdmin(whiteListUser.getAdmin());
        whiteListUserDto.setUpload(whiteListUser.getUpload());
        return whiteListUserDto;
    }

    public static WhiteListUser convertWhiteListUserDtoToUser(WhiteListUserDto whiteListUserDto, User user) {
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setId(whiteListUser.getId());
        whiteListUser.setUser(user);
//        whiteListUser.setCreateDate(whiteListUserDto.getCreateDate());
//        whiteListUser.setTrigger(whiteListUserDto.getTrigger());
        whiteListUser.setAdmin(whiteListUser.getAdmin());
        whiteListUser.setUpload(whiteListUser.getUpload());
        return whiteListUser;
    }

    public static String formatDate(WhiteListUser whiteListUser) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dtf.format(ZonedDateTime.ofLocal(whiteListUser.getTrigger(), ZoneId.of("Europe/Moscow"), ZoneOffset.UTC));
        return date;
    }
}
