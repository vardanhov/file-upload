package com.example.uploadfile.repo;

import com.example.uploadfile.domain.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long> {
    SecurityUser findSecurityUserByUsername(String username);
}
