package com.example.uploadfile.repo;

import com.example.uploadfile.domain.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserGroupsEntityRepository extends JpaRepository<UserGroups, Long> {
    Optional<UserGroups> findUserGroupsByGroupName(String groupName);

    @Transactional
    int removeById(Long id);
}
