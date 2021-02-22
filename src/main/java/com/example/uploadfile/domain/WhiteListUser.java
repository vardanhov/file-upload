package com.example.uploadfile.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "trigger")
    private LocalDateTime trigger;

    @Column(name = "admin", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean admin;

    @Column(name = "upload", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean upload;


//    @Convert(converter = UserProfileConverter.class)
//    @Column(name = "profile_id")
//    private UserProfile userProfile;

}
