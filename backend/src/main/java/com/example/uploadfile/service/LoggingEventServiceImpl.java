package com.example.uploadfile.service;

import com.example.uploadfile.domain.LoggingEvent;
import com.example.uploadfile.dto.UserAction;
import com.example.uploadfile.repo.LoggingEventRepository;
import com.example.uploadfile.service.iface.LoggingEventService;
import com.example.uploadfile.util.UserActionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoggingEventServiceImpl implements LoggingEventService {

    private final LoggingEventRepository loggingEventRepository;

    public LoggingEventServiceImpl(LoggingEventRepository loggingEventRepository) {
        this.loggingEventRepository = loggingEventRepository;
    }

    public List<UserAction> getAllEvent() {
        List<LoggingEvent> loggingEvents = loggingEventRepository.findAll();
        List<UserAction> userActions = loggingEvents.stream().map(loggingEvent -> UserActionMapper.toUserAction(loggingEvent)).collect(Collectors.toList());
        return userActions;
    }

    public Page<UserAction> getAllEvent(Pageable pageable) {
        Page<LoggingEvent> loggingEvents = loggingEventRepository.findAll(pageable);
        Page<UserAction> userActions = loggingEvents.map(loggingEvent -> UserActionMapper.toUserAction(loggingEvent));
        return userActions;
    }


    public List<UserAction> getUserActionByUserName(String username) {
        List<LoggingEvent> loggingEvents = loggingEventRepository.getLoggingEventByUserName(username);
        List<UserAction> userActions = loggingEvents.stream()
                .map(loggingEvent -> UserActionMapper.toUserAction(loggingEvent)).collect(Collectors.toList());
        return userActions;
    }

    public Page<UserAction> getUserActionByUserName(String username, Pageable pageable) {

        Page<LoggingEvent> loggingEvents = loggingEventRepository.getLoggingEventByUserName(username, pageable);
        Page<UserAction> userActions = loggingEvents.map(loggingEvent -> UserActionMapper.toUserAction(loggingEvent));
        return userActions;
    }

    public List<UserAction> getUserActionByUserNameAndActionType(String username, String actionType) {
        List<LoggingEvent> loggingEvents = loggingEventRepository.getLoggingEventByUserNameAndActionType(username, actionType);

        List<UserAction> userActions = loggingEvents.stream()
                .map(loggingEvent -> UserActionMapper.toUserAction(loggingEvent)).collect(Collectors.toList());
        return userActions;
    }

    public Page<UserAction> getUserActionByUserNameAndActionType(String username, String actionType, Pageable pageable) {
        Page<LoggingEvent> loggingEvents = loggingEventRepository.getLoggingEventByUserNameAndActionType(username, actionType, pageable);

        Page<UserAction> userActions = loggingEvents
                .map(loggingEvent -> UserActionMapper.toUserAction(loggingEvent));
        return userActions;
    }

    public File createFileAllEvents() {
        List<LoggingEvent> list = loggingEventRepository.findAll();

        File file = new File("logs/events-" + System.currentTimeMillis() + ".log");
        writeIntoFile(list, file);
        return file;
    }

    public File createFileUseEvents(String username) {
        List<LoggingEvent> list = loggingEventRepository.getLoggingEventByUserName(username);

        File file = new File("logs/user-events-" + System.currentTimeMillis() + ".log");
        writeIntoFile(list, file);
        return file;
    }

    private void writeIntoFile(List<LoggingEvent> list, File file) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(list.stream().map(LoggingEvent::toString).collect(Collectors.joining("\n")));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
