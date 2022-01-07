package com.example.uploadfile.util;

import com.example.uploadfile.domain.LoggingEvent;
import com.example.uploadfile.dto.UserAction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;


final public class UserActionMapper {

    private static final int INDEX_OF_MESSAGE = 2;
    private static final int INDEX_OF_FILENAME = 3;
    private static final int INDEX_OF_PATH = 4;
    private static final int INDEX_OF_ACTION_TYPE = 0;
    private static final int INDEX_OF_IS_NEW_DIRECTORY = 6;
    private static final int INDEX_OF_IS_DAG = 5;


    public static UserAction toUserAction(LoggingEvent loggingEvent) {
        List<String> descriptions = Arrays.asList(loggingEvent.getFormattedMessage().split("\\|"));

        UserAction userAction = new UserAction();
        userAction.setUserName(loggingEvent.getArg0());
        userAction.setDescription(descriptions.get(INDEX_OF_MESSAGE).replace("message=", ""));
        Instant instant = Instant.ofEpochMilli(loggingEvent.getTimestmp());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        userAction.setActionDateTime(localDateTime);
        userAction.setActionType(loggingEvent.getArg2());
        userAction.setId(loggingEvent.getEventId());
        userAction.setFileName(descriptions.get(INDEX_OF_FILENAME));
        userAction.setPath(descriptions.size() > INDEX_OF_PATH ? descriptions.get(INDEX_OF_PATH) : "");
        userAction.setActionType(descriptions.get(INDEX_OF_ACTION_TYPE).replace("[", "").replace("]", ""));
        userAction.setIsNewDirectory(descriptions.size() > INDEX_OF_IS_NEW_DIRECTORY ? descriptions.get(INDEX_OF_IS_NEW_DIRECTORY) : "");
        userAction.setIsDag(descriptions.size() > INDEX_OF_IS_DAG ? descriptions.get(INDEX_OF_IS_DAG).replace("isDag=", "") : "");

        return userAction;
    }
}
