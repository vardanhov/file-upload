package com.example.uploadfile.controller;

import com.example.uploadfile.domain.LoggingEvent;
import com.example.uploadfile.service.iface.LoggingEventService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class AdminControllerUnitTest {

    private final LoggingEventService eventService = mock(LoggingEventService.class);
    private final AdminController controller = new AdminController(eventService);

    @Test
    void getUserHistoryReturnPageEventsWithStatusOk() {
        LoggingEvent loggingEvent = LoggingEvent.builder().eventId(1l).timestmp(1615839523483l)
                .formattedMessage("[Authentication Failure]  | username=admin | message=Bad credentials | type=UserEvent")
                .loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .levelString("ERROR")
                .threadName("https-jsse-nio-1443-exec-7")
                .referenceFlag((short) 1)
                .arg0("admin").arg1("Bad credentials").arg2("UserEvent").arg3(null)
                .callerFilename("CustomAuthenticationFailureHandler.java")
                .callerClass("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .callerMethod("onAuthenticationFailure")
                .callerLine("25")
                .build();

        PageImpl<LoggingEvent> page = new PageImpl(Arrays.asList(loggingEvent),
                PageRequest.of(0, 10, Sort.by("timestmp").descending()),
                1);

        doReturn(page).when(eventService).getAllEvent(any());

        ResponseEntity<PageImpl<LoggingEvent>> response = (ResponseEntity<PageImpl<LoggingEvent>>) controller
                .getUserHistory(0, 10);

        assertNotNull(response, "Ответ не может быть null");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getTotalElements());

    }

    @Test
    void getUserHistoryByUserNameReturnPageEventsWithStatusOk() {
        String userName = "admin";
        LoggingEvent loggingEvent = LoggingEvent.builder().eventId(1l).timestmp(1615839523483l)
                .formattedMessage("[Authentication Failure]  | username=admin | message=Bad credentials | type=UserEvent")
                .loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .levelString("ERROR").threadName("https-jsse-nio-1443-exec-7")
                .referenceFlag((short) 1)
                .arg0("admin").arg1("Bad credentials").arg2("UserEvent").arg3(null)
                .callerFilename("CustomAuthenticationFailureHandler.java")
                .callerClass("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .callerMethod("onAuthenticationFailure")
                .callerLine("25")
                .build();

        PageImpl<LoggingEvent> page = new PageImpl(Arrays.asList(loggingEvent),
                PageRequest.of(0, 10, Sort.by("timestmp").descending()),
                1);

        doReturn(page).when(eventService).getUserActionByUserName(eq(userName), any());

        ResponseEntity<PageImpl<LoggingEvent>> response = (ResponseEntity<PageImpl<LoggingEvent>>) controller.
                getUsrHistory(0, 10, userName);

        assertNotNull(response, "Ответ не может быть null");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(userName, response.getBody().getContent().stream().findFirst().get().getArg0());
        assertEquals("Bad credentials", response.getBody().getContent().stream().findFirst().get().getArg1());
        assertEquals("UserEvent", response.getBody().getContent().stream().findFirst().get().getArg2());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getTotalElements());

    }

    @Test
    void getUserHistoryByUserNameAndEventTypeReturnPageEventsWithStatusOk() {
        String userName = "admin";
        String eventType = "UserEvent";
        LoggingEvent loggingEvent = LoggingEvent.builder().eventId(1l).timestmp(1615839523483l)
                .formattedMessage("[Authentication Failure]  | username=admin | message=Bad credentials | type=UserEvent")
                .loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .levelString("ERROR")
                .threadName("https-jsse-nio-1443-exec-7")
                .referenceFlag((short) 1)
                .arg0("admin").arg1("Bad credentials").arg2("UserEvent").arg3(null)
                .callerFilename("CustomAuthenticationFailureHandler.java")
                .callerClass("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .callerMethod("onAuthenticationFailure")
                .callerLine("25")
                .build();

        PageImpl<LoggingEvent> page = new PageImpl(Arrays.asList(loggingEvent),
                PageRequest.of(0, 10, Sort.by("timestmp").descending()),
                1);

        doReturn(page).when(eventService).getUserActionByUserNameAndActionType(eq(userName), eq(eventType), any());

        ResponseEntity<PageImpl<LoggingEvent>> response = (ResponseEntity<PageImpl<LoggingEvent>>) controller.
                getUsrHistory(0, 10, userName, eventType);

        assertNotNull(response, "Ответ не может быть null");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(userName, response.getBody().getContent().stream().findFirst().get().getArg0());
        assertEquals("Bad credentials", response.getBody().getContent().stream().findFirst().get().getArg1());
        assertEquals("UserEvent", response.getBody().getContent().stream().findFirst().get().getArg2());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getTotalElements());

    }

}
