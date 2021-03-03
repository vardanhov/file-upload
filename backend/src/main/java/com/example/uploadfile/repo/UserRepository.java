package com.example.uploadfile.repo;

import com.example.uploadfile.domain.User;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends LdapRepository<User> {
    User findUserByUsername(String username);
}
