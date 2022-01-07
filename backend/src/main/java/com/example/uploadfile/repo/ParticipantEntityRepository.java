package com.example.uploadfile.repo;

import com.example.uploadfile.domain.AccessUserListEntity;
import com.example.uploadfile.domain.LoggingEvent;
import com.example.uploadfile.domain.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParticipantEntityRepository extends JpaRepository<ParticipantEntity, Long> {
    Optional<ParticipantEntity> findParticipantEntityByUsername(String username);

    ParticipantEntity findByUsername(String username);

    @Transactional
    int deleteByUsername(String username);

    @Query(value = "select * from upload.participant p where p.group_id = ?1", nativeQuery = true)
    List<ParticipantEntity> findParticipantEntitiesByGroupId(Long id);
}
