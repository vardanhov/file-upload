package com.example.uploadfile.util;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public static String formatDate(WhiteListUser whiteListUser) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dtf.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(whiteListUser.getTrigger()), ZoneId.of("Europe/Moscow")));
        return date;
    }
}
