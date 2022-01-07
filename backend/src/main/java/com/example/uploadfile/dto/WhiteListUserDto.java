package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

//FiXME Rename
@Data
@NoArgsConstructor
@ToString
public class WhiteListUserDto {

    private Long id;

    private String userName;

    private String fullName;

    private String group;

    private LocalDateTime createDate;

    private String role;

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

    private String addedBy;

    private Boolean isNative;

}
