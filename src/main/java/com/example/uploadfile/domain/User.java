package com.example.uploadfile.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Entry;
import javax.naming.Name;



@Data
@NoArgsConstructor
@ToString
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"}, base = "ou=people")
public class User {

    @Id
    private Name id;

    @Attribute(name = "uid")
    private String username;

    @Attribute(name = "sn")
    private String lastname;

    @Attribute(name = "cn")
    private String fullName;

    @Attribute(name = "userPassword")
    private String password;

    private Group group;



    }

