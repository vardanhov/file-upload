package com.example.uploadfile.repo;

import com.example.uploadfile.domain.LoggingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggingEventRepository extends JpaRepository<LoggingEvent, Long> {

    @Query(value = "select * from upload.logging_event u where u.arg0= :username", nativeQuery = true)
    List<LoggingEvent> getLoggingEventByUserName(String username);

    @Query(value = "select * from upload.logging_event u where u.arg0= :username", nativeQuery = true)
    Page<LoggingEvent> getLoggingEventByUserName(String username, Pageable pageable);

    @Query(value = "select * from upload.logging_event u where u.arg0= :username and u.arg2= :actionType", nativeQuery = true)
    List<LoggingEvent> getLoggingEventByUserNameAndActionType(String username, String actionType);

    @Query(value = "select * from upload.logging_event u where u.arg0= :username and u.arg2= :actionType", nativeQuery = true)
    Page<LoggingEvent> getLoggingEventByUserNameAndActionType(String username, String actionType, Pageable pageable);
}
