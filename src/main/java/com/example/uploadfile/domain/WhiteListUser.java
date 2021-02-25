package com.example.uploadfile.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="white_list_user")
@Data
@NoArgsConstructor
@ToString
public class WhiteListUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "trigger")
    private Long trigger;

    @Column(name = "admin", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean admin;

    @Column(name = "upload", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean upload;


}
