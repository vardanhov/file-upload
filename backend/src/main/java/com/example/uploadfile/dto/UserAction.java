package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@ToString
public class UserAction {

    private Long id;

    private String userName;

    private String fileName;

    private String path;

    private String actionType;

    private String isNewDirectory;

    private String isDag;

    private String description;

    private LocalDateTime actionDateTime;
}
