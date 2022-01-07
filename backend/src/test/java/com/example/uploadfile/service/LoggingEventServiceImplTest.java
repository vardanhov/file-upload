package com.example.uploadfile.service;

import com.example.uploadfile.domain.LoggingEvent;
import com.example.uploadfile.dto.UserAction;
import com.example.uploadfile.repo.LoggingEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LoggingEventServiceImplTest {

    private final LoggingEventRepository repository = mock(LoggingEventRepository.class);
    private final LoggingEventServiceImpl service = new LoggingEventServiceImpl(repository);

    @Test
    public void getAllEventReturnListUserAction() {
        List<LoggingEvent> eventList = LongStream.range(1, 4).mapToObj(i -> LoggingEvent.builder().
                eventId(i).timestmp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).
                formattedMessage("[Authentication Failure]  | username=asdf | message=Bad credentials | тестовый файл | тестовая директория | даг | новая директория ").
                loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler").
                levelString("ERROR").
                arg0("asdf").arg1("Bad credentials").arg2("UserEvent").arg3("").build()
        ).collect(Collectors.toList());

        doReturn(eventList).when(this.repository).findAll();

        List<UserAction> allEvent = service.getAllEvent();

        assertNotNull(allEvent);
        assertEquals(3, allEvent.size(), "Ожидается 4 элемента");
    }

    @Test
    public void getEventByUserNameReturnListUserAction() {
        List<LoggingEvent> eventList = Stream.of(LoggingEvent.builder()
                .eventId(1L)
                .timestmp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .formattedMessage("[Authentication Failure]  | username=asdf | message=Bad credentials | тестовый файл | тестовая директория | даг | новая директория ")
                .loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .levelString("ERROR")
                .arg0("USER1").arg1("Bad credentials").arg2("UserEvent").arg3("")
                .build())
                .collect(Collectors.toList());

        doReturn(eventList).when(this.repository).getLoggingEventByUserName(anyString());

        List<UserAction> userActionList = service.getUserActionByUserName("USER1");

        assertNotNull(userActionList);
        assertEquals(1, userActionList.size(), "Ожидается 1 элемент");
        assertEquals(userActionList.stream().findFirst().get().getUserName(), "USER1");
    }

    @Test
    public void getEventByUserNameAndTypeEventReturnListUserAction() {
        List<LoggingEvent> eventList = Stream.of(LoggingEvent.builder()
                .eventId(1L)
                .timestmp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .formattedMessage("[Authentication Failure]  | username=asdf | message=Bad credentials | тестовый файл | тестовая директория | даг | новая директория ")
                .loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .levelString("ERROR")
                .arg0("USER1").arg1("Bad credentials").arg2("UserEvent").arg3("")
                .build())
                .collect(Collectors.toList());

        doReturn(eventList).when(this.repository).getLoggingEventByUserNameAndActionType(anyString(), eq("UserEvent"));

        List<UserAction> userActionList = service.getUserActionByUserNameAndActionType("USER1", "UserEvent");

        assertNotNull(userActionList);
        assertEquals(1, userActionList.size(), "Ожидается 1 элемент");
        assertEquals(userActionList.stream().findFirst().get().getUserName(), "USER1");
        assertEquals(userActionList.stream().findFirst().get().getActionType(), "Authentication Failure  ");
    }

}
