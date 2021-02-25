package com.example.uploadfile.repo;

import com.example.uploadfile.domain.WhiteListUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface WhiteListUserRepository extends JpaRepository<WhiteListUser, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE upload.white_list_user  SET upload=B'0' where trigger< (select upload.current_time_millisecond()) AND upload=B'1'", nativeQuery = true)
    void changePermissions();

}
