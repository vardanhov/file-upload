package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


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