package com.example.uploadfile.data;


import com.example.uploadfile.domain.Authorities;
import com.example.uploadfile.domain.User;
import com.example.uploadfile.repo.AuthoritiesRepository;
import com.example.uploadfile.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopulateDatabaseService {

    @Bean
    public CommandLineRunner populate(final UserRepository userRepository,
                                      final AuthoritiesRepository authoritiesRepository) {
        return args -> {
            List<User> users = userRepository.findAll();
            List<Authorities> authorities = authoritiesRepository.findAll();

        };
    }
}
