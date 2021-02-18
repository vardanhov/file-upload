package com.example.uploadfile.config;

import com.example.uploadfile.domain.Authorities;
import com.example.uploadfile.domain.User;
import com.example.uploadfile.repo.AuthoritiesRepository;
import com.example.uploadfile.repo.UserRepository;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private final AuthoritiesRepository authoritiesRepository;
    private final UserRepository userRepository;

    public CustomAuthoritiesPopulator(AuthoritiesRepository authoritiesRepository, UserRepository userRepository) {
        this.authoritiesRepository = authoritiesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations dirContextOperations, String username) {

        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        final Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            final List<Authorities> authorities = authoritiesRepository.findAllByUser(user.get());
            grantedAuthorities = authorities.stream()
                    .map(authorities1 -> new SimpleGrantedAuthority(authorities1.getAuthority()))
            .collect(Collectors.toList());
        }
        return grantedAuthorities;
    }
}
