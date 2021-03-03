package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;

   private String username;

   private String password;

}
