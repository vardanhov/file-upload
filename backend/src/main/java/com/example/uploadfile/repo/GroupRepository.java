package com.example.uploadfile.repo;

import com.example.uploadfile.domain.Group;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GroupRepository extends LdapRepository<Group> {


    List<Group> findGroupsByName(String uniq);


    List<Group> findGroupsByMembersIsLike(String uid);
}
