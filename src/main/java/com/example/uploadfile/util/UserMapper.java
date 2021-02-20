package com.example.uploadfile.util;

import com.example.uploadfile.domain.User;
import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        whiteListUserDto.setCreateDate(convertToString(whiteListUser.getCreateDate()));
        whiteListUserDto.setTrigger(convertToString(whiteListUser.getTrigger()));
        whiteListUserDto.setAdmin(whiteListUser.getAdmin());
        whiteListUserDto.setUpload(whiteListUser.getUpload());
        return whiteListUserDto;
    }

    public static WhiteListUser convertWhiteListUserDtoToUser(WhiteListUserDto whiteListUserDto, User user) {
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setUser(user);
        whiteListUser.setCreateDate(convertToLong(whiteListUserDto.getCreateDate()));
        whiteListUser.setTrigger(convertToLong(whiteListUserDto.getTrigger()));
        whiteListUser.setAdmin(whiteListUserDto.getAdmin());
        whiteListUser.setUpload(whiteListUserDto.getUpload());
        return whiteListUser;
    }

    public static String convertToString(Long dateMillisecond) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dtf.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateMillisecond), ZoneId.of("Europe/Moscow")));
        return date;
    }

    public static Long convertToLong(String dtoDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dtoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }
}
