package com.example.uploadfile.service.iface;

import com.example.uploadfile.dto.UserAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface LoggingEventService {

    List<UserAction> getAllEvent();

    Page<UserAction> getAllEvent(Pageable pageable);

    List<UserAction> getUserActionByUserName(String username);

    Page<UserAction> getUserActionByUserName(String username, Pageable pageable);

    List<UserAction> getUserActionByUserNameAndActionType(String username, String actionType);

    Page<UserAction> getUserActionByUserNameAndActionType(String username, String actionType, Pageable pageable);

    File createFileAllEvents();

    File createFileUseEvents(String username);
}
