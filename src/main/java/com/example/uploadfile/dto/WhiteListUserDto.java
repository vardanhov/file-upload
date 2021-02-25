package com.example.uploadfile.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "upload_file", schema = "upload")

public class WhiteListUserDto {

    private Integer id;

    private String name;

    private String username;

    private String createDate;

    private String trigger;

    private Boolean admin;

    private Boolean upload;



    //TODO добавить группу из АД
    //TODO доабавить ФИО
}