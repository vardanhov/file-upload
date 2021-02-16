package com.example.uploadfile.repo;

import com.example.uploadfile.domain.WhiteListUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WhiteListUserRepository extends JpaRepository<WhiteListUser,Integer> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE upload.white_list_user  SET upload=B'0' where trigger<NOW() AND upload=B'1'", nativeQuery = true)
    void changePermissions();



//    @Query("update User u set u.active = false where u.lastLoginDate < :date")
//    void deactivateUsersNotLoggedInSince(@Param("date") LocalDate date);

//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE Users u set EMAIL_VERIFICATION_STATUS =:emailVerificationStatus where u.USER_ID = :userId",
//            nativeQuery = true)
//    void updateUser(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);
}
