package com.example.uploadfile.config;



import com.example.uploadfile.data.authorities.AuthoritiesRepository;
import com.example.uploadfile.data.user.MyUserRepository;
import com.example.uploadfile.service.ConfigAdminUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;


@EnableWebSecurity
@Slf4j
@Order(1)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(LdapProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthoritiesRepository authoritiesRepository;
    private final MyUserRepository myUserRepository;

    public WebSecurityConfig(AuthoritiesRepository authoritiesRepository, MyUserRepository myUserRepository) {
        this.authoritiesRepository = authoritiesRepository;
        this.myUserRepository = myUserRepository;
    }

    @Autowired
    ConfigAdminUserDetailsService configAdminUserDetailsService;

    @Autowired
    private LdapProperties ldapProperties;

    @Autowired(required = false)
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired(required = false)
    ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider;

//    @Value("${application.ldap.domain}")
//    private String domain;
//
//    @Value("${application.ldap.url}")
//    private String url;
//
//    @Value("${application.ldap.rootDn}")
//    private String rootDn;


    @Bean
    @ConditionalOnProperty(name = "spring.customized.suite.auth_type", havingValue = "local_user")
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(configAdminUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                UserDetails userDetails = super.mapUserFromContext(ctx, username, authorities);
                try {
                    userDetails = configAdminUserDetailsService.loadUserByUsername(userDetails.getUsername());
                    return userDetails;
                } catch (Exception ex) {
                    throw new DisabledException(ex.getMessage());
                }
            }
        };
    }



    @Bean
    @ConditionalOnProperty(name = "spring.customized.suite.auth_type", havingValue = "ldap")
    ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider = new ActiveDirectoryLdapAuthenticationProvider(null, ldapProperties.getUrls() != null && ldapProperties.getUrls().length >= 1 ? ldapProperties.getUrls()[0] : null, ldapProperties.getBase());
        activeDirectoryLdapAuthenticationProvider.setUserDetailsContextMapper(userDetailsContextMapper());
        return activeDirectoryLdapAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .ldapAuthoritiesPopulator(new CustomAuthoritiesPopulator(authoritiesRepository, myUserRepository))
                .userDetailsContextMapper(new CustomUserDetailsMapper())
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .root("dc=springframework,dc=org")
                .ldif("classpath:test-ldap.ldif")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll() // Open for all
                .anyRequest().authenticated() // All others requires authentication
                .and()
                .formLogin()
                .defaultSuccessUrl("/hello", true)
                .and()
                .logout().logoutSuccessUrl("/");
    }

//    @Override
//    public void configure(WebSecurity webSecurity) throws Exception {
//        webSecurity.ignoring().antMatchers("/static/**");
//        super.configure(webSecurity);
//    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/h2-console/**"
        );

    }
}