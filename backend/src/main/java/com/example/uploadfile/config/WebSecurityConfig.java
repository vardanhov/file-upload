package com.example.uploadfile.config;

import com.example.uploadfile.domain.AccessUserListEntity;
import com.example.uploadfile.domain.enums.UserProfileType;
import com.example.uploadfile.excepion.UserNotFoundException;
import com.example.uploadfile.repo.AccessUserListEntityRepository;
import com.example.uploadfile.security.CustomAuthenticationFailureHandler;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import javax.sql.DataSource;
import java.util.Set;


@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(LdapProperties.class)
@Profile("!dev")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LdapGroupProperties groupProperties;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AccessUserListEntityRepository accessUserListEntityRepository;

    @Value("${application.ldap.provider.url}")
    private String ldapProviderUrl;

    @Value("${application.ldap.provider.userdn}")
    private String ldapProviderUserDn;

    @Value("${application.ldap.provider.password}")
    private String ldapProviderPassword;

    @Value("${application.ldap.user.dn.patterns}")
    private String ldapUserDnPatterns;

    @Value("${application.ldap.group.search.base}")
    private String ldapGroupSearchBase;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .failureHandler(failureHandler).permitAll()
                .defaultSuccessUrl("/upload", true)
                .and()

                .logout()
                .logoutSuccessUrl("/login")
                .and()

                .authorizeRequests()
                .antMatchers("/upload")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.USER.name(), UserProfileType.VIEWER.name())

                .antMatchers("/api/users/history")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.USER.name(), UserProfileType.VIEWER.name())

                .antMatchers("/api/users/find-by-username")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.USER.name(), UserProfileType.VIEWER.name())

                .antMatchers("/api/users/all-histories")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.AUDITOR.name())

                .antMatchers("/api/groups/delete")
                .hasAnyRole(UserProfileType.ADMIN.name())

                .antMatchers("/api/users/get-audit-logs")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.AUDITOR.name())

                .antMatchers("/api/groups/create")
                .hasRole(UserProfileType.ADMIN.name())

                .antMatchers("/api/groups/delete/{\\d+}")
                .hasRole(UserProfileType.ADMIN.name())

                .antMatchers("/api/upload")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.USER.name())

                .antMatchers("/api/upload/check-file")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.USER.name())

                .antMatchers("/api/upload/check-access")
                .hasAnyRole(UserProfileType.ADMIN.name(), UserProfileType.USER.name())

                .antMatchers("/api/**").hasRole(UserProfileType.ADMIN.name())
                .antMatchers("/admin/**").hasRole(UserProfileType.ADMIN.name())

                .anyRequest()
                .authenticated()
                .and()

                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns(ldapUserDnPatterns)
                .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
                .contextSource(contextSource());

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select p.username username, u.password \"password\", u.enabled  enabled \n"
                                + "from upload.participant as p \n"
                                + "inner join upload.users as u \n"
                                + "on p.id = u.user_id \n"
                                + "where p.username=?")
                .authoritiesByUsernameQuery(
                        "SELECT p.username username, a.role \"role\""
                                + " FROM upload.participant as p"
                                + " inner join upload.access_user_list  as a"
                                + " on p.id = a.user_id"
                                + " where p.username=?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
        final DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource(), ldapGroupSearchBase) {
            @Override
            public Set<GrantedAuthority> getGroupMembershipRoles(final String userDn, final String username) {
                AccessUserListEntity whiteListUserByUserName = accessUserListEntityRepository.getWhiteListUserByUserName(username);

                if (whiteListUserByUserName == null) {
                    throw new UserNotFoundException("Не найден пользователь в WhiteList");
                }

                return ImmutableSet.of((GrantedAuthority) whiteListUserByUserName::getRole);
            }
        };

        authoritiesPopulator.setGroupRoleAttribute(groupProperties.getAttribute());
        authoritiesPopulator.setRolePrefix(groupProperties.getPrefix());
        authoritiesPopulator.setGroupSearchFilter(groupProperties.getFilter());

        return authoritiesPopulator;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        ldapTemplate.setContextSource(contextSource());
        return ldapTemplate;
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {

        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
                ldapProviderUrl);
        contextSource.setUserDn(ldapProviderUserDn);
        contextSource.setPassword(ldapProviderPassword);

        return contextSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
