package com.example.uploadfile.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.Set;


@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(LdapProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LdapGroupProperties groupProperties;

    @Value("${ldap.connection.url}")
    private String ldapConnectionUrl;

    @Value("${ldap.urls}")
    private String ldapUrls;

    @Value("${ldap.base.dn}")
    private String ldapBaseDn;

    @Value("${ldap.username}")
    private String ldapSecurityPrincipal;

    @Value("${ldap.password}")
    private String ldapPrincipalPassword;

    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO csrf задизейблены временно
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll() // Open for all
                .anyRequest().authenticated() // All others requires authentication
                .and()
                .formLogin()
                .defaultSuccessUrl("/upload", true)
                .and()
                .logout().logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .contextSource()
                .url(ldapUrls + ldapBaseDn)
                .managerDn(ldapSecurityPrincipal)
                .managerPassword(ldapPrincipalPassword)
                .and()
                .userDnPatterns(ldapUserDnPattern);
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {

        final DefaultLdapAuthoritiesPopulator authoritiesPopulator =
                new DefaultLdapAuthoritiesPopulator(contextSource(),
                        "ou=groups") {
                    @Override
                    public Set<GrantedAuthority> getGroupMembershipRoles(final String userDn,
                                                                         final String username) {
                        final Set<GrantedAuthority> groupMembershipRoles =
                                super.getGroupMembershipRoles(userDn, username);
                        return groupMembershipRoles;
                    }
                };
        authoritiesPopulator.setGroupRoleAttribute(groupProperties.getAttribute());
        authoritiesPopulator.setRolePrefix(groupProperties.getPrefix());
        authoritiesPopulator.setGroupSearchFilter(groupProperties.getFilter());
        return authoritiesPopulator;
    }


    @Bean
    public LdapTemplate ldapTemplate()  {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        ldapTemplate.setContextSource(contextSource());
        return ldapTemplate;
    }
//    @Bean
//    public LdapContextSource contextSource() throws Exception{
//        LdapContextSource ldapContextSource = new LdapContextSource();
//        ldapContextSource.setUrl("ldap://54.154.65.69:389");
//        ldapContextSource.setBase("dc=example,dc=org");
//        ldapContextSource.setUserDn("daniel@example");
//        ldapContextSource.setPassword("Test1234");
//        return ldapContextSource;
//    }


    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(ldapConnectionUrl);
    }
}
