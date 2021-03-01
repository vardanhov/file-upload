package com.example.uploadfile.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@ToString
@Entry(objectClasses = {"groupOfUniqueNames", "top"}, base = "ou=groups")
public class Group {
    @Id
    private Name id;

    @Attribute(name = "cn")
    @DnAttribute(value = "cn", index = 1)
    private String name;

    @Attribute(name = "description")
    private String description;

    @Attribute(name = "uniqueMember")
    private Set<Name> members = new HashSet<>();

}