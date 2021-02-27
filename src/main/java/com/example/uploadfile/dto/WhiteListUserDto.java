package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@ToString
public class WhiteListUserDto {

    private Integer id;

    private String userName;

    private String fullName;

    private String group;

    private LocalDateTime createDate;

    private Boolean admin;

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

}