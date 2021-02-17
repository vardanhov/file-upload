package com.example.uploadfile.dto;

import com.example.uploadfile.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@ToString
public class WhiteListUserDto {

    private Integer id;

    private String username;

    private String createDate;

    private String trigger;

    private Boolean admin;

    private Boolean upload;
}