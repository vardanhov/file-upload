package com.example.uploadfile.repo;

import com.example.uploadfile.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Modifying
    @Transactional
    @Query(value = "update user  SET upload=0 where trigger>NOW() AND upload=1", nativeQuery = true)
    int changePremissions();
}
