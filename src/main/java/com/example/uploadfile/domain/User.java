package com.example.uploadfile.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Data
@NoArgsConstructor
@ToString
public class User {

    private Integer id;

    private String username;

    private String password;

    public User(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("username", username)
                .append("password", password)
                .toString();
    }
}
