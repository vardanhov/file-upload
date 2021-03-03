package com.example.uploadfile.repo;

import com.example.uploadfile.domain.Group;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends LdapRepository<Group> {
}
