package com.example.uploadfile.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Entry;
import javax.naming.Name;



@Data
@NoArgsConstructor
@ToString
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"}, base = "ou=Departments")
public class User {

    @Id
    private Name id;

    private String uid;

    private String username;

    private String password;



    }

