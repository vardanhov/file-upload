package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;

    private String login;

    private Date adding;

    private Date trigger;

    private Boolean admin;

    private Boolean upload;


}
