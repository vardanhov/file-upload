package com.example.uploadfile.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="white_list_user")
@Data
@NoArgsConstructor
@ToString
@DynamicUpdate
@DynamicInsert
public class WhiteListUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name="full_name")
    private String fullName;

    @Column
    private String group;

    @Column(name = "create_date")
    private Long createDate;

    @Column
    private Long from;

    @Column
    private Long to;

    @Column
    private Integer admin;



}

