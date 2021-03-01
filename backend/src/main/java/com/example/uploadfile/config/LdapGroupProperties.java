package com.example.uploadfile.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ldap.group.role")
public class LdapGroupProperties {
    private String attribute = "cn";
    private String prefix = "ROLE_";
    private String filter = "member={0}";
}
