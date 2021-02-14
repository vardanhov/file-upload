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

//    @Modifying
//    @Query("update User u set u.active = false where u.lastLoginDate < :date")
//    void deactivateUsersNotLoggedInSince(@Param("date") LocalDate date);

//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE Users u set EMAIL_VERIFICATION_STATUS =:emailVerificationStatus where u.USER_ID = :userId",
//            nativeQuery = true)
//    void updateUser(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);
}
