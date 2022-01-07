package com.example.uploadfile.security;

import com.example.uploadfile.domain.SecurityUser;
import com.example.uploadfile.repo.SecurityUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;


@Slf4j
@Component
public class UpdatePasswordService {
    @Value("${mail.configure.admin.enabled}")
    private boolean passwordShouldBeUpdate;

    @Value("${mail.configure.admin.username}")
    private String adminUserName;

    private final JavaMailSender emailSender;
    private final SecurityUserRepository securityUserRepository;

    @Autowired
    public UpdatePasswordService(JavaMailSender emailSender, SecurityUserRepository securityUserRepository) {
        this.emailSender = emailSender;
        this.securityUserRepository = securityUserRepository;
    }

    @PostConstruct
    public void generateAndSendAdminPassword() {
        if (passwordShouldBeUpdate) {
            SecurityUser securityUser = securityUserRepository.findSecurityUserByUsername(adminUserName);
            String newPassword = UUID.randomUUID().toString();
            securityUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            securityUserRepository.save(securityUser);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(securityUser.getEmail());
            message.setSubject("Password for login");
            message.setText(newPassword);

            emailSender.send(message);
        }
    }

}
