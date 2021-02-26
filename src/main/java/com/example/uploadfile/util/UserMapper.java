package com.example.uploadfile.util;

import com.example.uploadfile.domain.WhiteListUser;
import com.example.uploadfile.dto.WhiteListUserDto;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserMapper {

    public static WhiteListUserDto toWhiteListUserDto(WhiteListUser whiteListUser) {
        WhiteListUserDto whiteListUserDto = new WhiteListUserDto();
        whiteListUserDto.setId(whiteListUser.getId());
        whiteListUserDto.setUsername(whiteListUser.getUserName());
        whiteListUserDto.setFullName(whiteListUser.getFullName());
        whiteListUserDto.setGroup(whiteListUser.getGroup());
        whiteListUserDto.setCreateDate(toLocalDate(whiteListUser.getCreateDate()));
        whiteListUserDto.setFrom(toLocalDate(whiteListUser.getFrom()));
        whiteListUserDto.setTo(toLocalDate(whiteListUser.getTo()));
        whiteListUserDto.setAdmin(whiteListUser.getAdmin());
        return whiteListUserDto;
    }

    public static WhiteListUser toWhiteListUser(WhiteListUserDto whiteListUserDto) {
        WhiteListUser whiteListUser = new WhiteListUser();
        whiteListUser.setUserName(whiteListUserDto.getUsername());
        whiteListUser.setFullName(whiteListUserDto.getFullName());
        whiteListUser.setGroup(whiteListUserDto.getGroup());
        whiteListUser.setCreateDate(toLong(whiteListUserDto.getCreateDate()));
        whiteListUser.setFrom(toLong(whiteListUserDto.getFrom()));
        whiteListUser.setTo(toLong(whiteListUserDto.getTo()));
        whiteListUser.setAdmin(whiteListUserDto.getAdmin());
        return whiteListUser;
    }

    public static LocalDateTime toLocalDate(Long dateMillisecond) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dtf.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateMillisecond), ZoneId.of("Europe/Moscow")));
        return LocalDateTime.parse(date, dtf);
    }

    public static Long toLong(LocalDateTime dtoDate) {
        ZonedDateTime zdt = ZonedDateTime.of(dtoDate, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    public static Boolean toBolean(Integer i) {
        return i == 1;
    }

    public static Integer toInteger(Boolean bool) {
        if (bool) return 1;
        else return 0;
    }
}
