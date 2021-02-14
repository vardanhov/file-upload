package com.example.uploadfile.data;



import com.example.uploadfile.data.authorities.Authorities;
import com.example.uploadfile.data.authorities.AuthoritiesRepository;
import com.example.uploadfile.data.user.MyUser;
import com.example.uploadfile.data.user.MyUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PopulateDatabaseService {

    @Bean
    public CommandLineRunner populate(final MyUserRepository myUserRepository,
                                      final AuthoritiesRepository authoritiesRepository) {
        return args -> {

            List<MyUser> users = Arrays.asList(
                    new MyUser("andy"),
                    new MyUser("ben"));

            myUserRepository.saveAll(users);

            List<Authorities> authorities = Arrays.asList(
                    new Authorities(users.get(0), "ROLE_USER"),
                    new Authorities(users.get(1), "ROLE_USER"),
                    new Authorities(users.get(1), "ROLE_ADMIN")
            );
            authoritiesRepository.saveAll(authorities);
        };
    }
}
