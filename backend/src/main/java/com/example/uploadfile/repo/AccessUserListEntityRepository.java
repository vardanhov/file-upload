package com.example.uploadfile.repo;

import com.example.uploadfile.domain.AccessUserListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessUserListEntityRepository extends JpaRepository<AccessUserListEntity, Long> {
    @Transactional
    @Modifying
    @Query(value = "update AccessUserListEntity w set w.role = :role where w.role='ROLE_USER'"
            + " and ((w.dateFrom >:localDateTime or w.dateFrom is null ) or (w.dateTo < :localDateTime or w.dateTo is null))")
    int deactivateUser(@Param("localDateTime") LocalDateTime localDateTime, @Param("role") String role);

    @Transactional
    @Modifying
    @Query(value = "update AccessUserListEntity w set w.role = :role where w.role='ROLE_VIEWER' "
            + "and :localDateTime between w.dateFrom and w.dateTo")
    int activateUser(@Param("localDateTime") LocalDateTime localDateTime, @Param("role") String role);

    @Query(value = "select w from AccessUserListEntity w where w.role=:role and :localDateTime not between w.dateFrom and w.dateTo")
    List<AccessUserListEntity> findWhiteListUsersToDeactivate(@Param("localDateTime") LocalDateTime localDateTime, @Param("role") String role);

    @Query(value = "select w from AccessUserListEntity w where w.role=:role and :localDateTime between w.dateFrom and w.dateTo")
    List<AccessUserListEntity> findWhiteListUsersToActivate(@Param("localDateTime") LocalDateTime localDateTime, @Param("role") String role);

    @Query(value = "select w from AccessUserListEntity w where w.participantEntity.username=:userName")
    AccessUserListEntity getWhiteListUserByUserName(@Param("userName") String username);

}
