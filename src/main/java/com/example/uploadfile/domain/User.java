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
@Table(name="user")
@Data
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    public User(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("username", username)
                .append("isEnabled", isEnabled)
                .toString();
    }
}
