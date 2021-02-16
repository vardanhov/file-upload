package com.example.uploadfile.repo;

import com.example.uploadfile.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE upload.white_list_user  SET upload=B'0' where trigger<NOW() AND upload=B'1'", nativeQuery = true)
    void changePermissions();

    Optional<User> findByUsername(String username);


}
