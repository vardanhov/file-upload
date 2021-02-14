package com.example.uploadfile.domain;


import com.example.uploadfile.domain.enums.UserProfile;
import com.example.uploadfile.domain.enums.convert.UserProfileConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="white_list_user")
@Data
@NoArgsConstructor
@ToString
public class WhiteListUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private User user;

    @Column(name = "createDate")
    @CreationTimestamp
    private Date createDate;

    @Column(name = "trigger")
    private Date trigger;

    @Column(name = "admin", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean admin;

    @Column(name = "upload", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean upload;

    @Convert(converter = UserProfileConverter.class)
    @Column(name = "profile_id")
    private UserProfile userProfile;
}
