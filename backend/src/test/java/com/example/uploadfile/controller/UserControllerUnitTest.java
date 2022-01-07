package com.example.uploadfile.controller;

import com.example.uploadfile.domain.LoggingEvent;
import com.example.uploadfile.excepion.InvalidOldPasswordException;
import com.example.uploadfile.service.iface.GroupService;
import com.example.uploadfile.service.iface.LoggingEventService;
import com.example.uploadfile.service.iface.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class UserControllerUnitTest {

    private final UserService userService = mock(UserService.class);
    private final LoggingEventService eventService = mock(LoggingEventService.class);
    private final GroupService groupService = mock(GroupService.class);
    private final UserController controller = new UserController(userService, eventService, groupService);

    @Test
    void getUsrHistoryReturnPageEventsWithStatusOk() {
        String userName = "user";
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new TestingAuthenticationToken(userName, auth);
        LoggingEvent loggingEvent = LoggingEvent.builder().eventId(1l).timestmp(1615839523483l)
                .formattedMessage("[Authentication Failure]  | username=admin | message=Bad credentials | type=UserEvent")
                .loggerName("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .levelString("ERROR")
                .threadName("https-jsse-nio-1443-exec-7")
                .referenceFlag((short) 1)
                .arg0("user").arg1("Bad credentials").arg2("UserEvent").arg3(null)
                .callerFilename("CustomAuthenticationFailureHandler.java")
                .callerClass("com.example.uploadfile.security.CustomAuthenticationFailureHandler")
                .callerMethod("onAuthenticationFailure").callerLine("25")
                .build();

        PageImpl<LoggingEvent> page = new PageImpl(Arrays.asList(loggingEvent),
                PageRequest.of(0, 10, Sort.by("timestmp").descending()),
                1);

        doReturn(page).when(eventService).getUserActionByUserName(eq(userName), any());

        ResponseEntity<PageImpl<LoggingEvent>> response = (ResponseEntity<PageImpl<LoggingEvent>>) controller
                .getUsrHistory(0, 10, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(userName, response.getBody().getContent().stream().findFirst().get().getArg0());
    }

    @Test
    public void updatePassword_CorrectOldPassword_ReturnSuccessWithStatusOk() {
        String userName = "user";
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new TestingAuthenticationToken(userName, auth);
        doNothing().when(userService).updatePassword(any(), any(), eq(userName));

        ResponseEntity<String> response = controller.updatePassword("321", "123", authentication);

        assertNotNull(response, "Ответ не должен быть NULL");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Код ответа должен быть 200");
        assertEquals("Success", response.getBody());


    }

    @Test
    public void updatePassword_IncorrectOldPassword_ReturnResponseWithStatusUnauthorized() {
        String userName = "user";
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new TestingAuthenticationToken(userName, auth);
        doThrow(InvalidOldPasswordException.class).when(userService).updatePassword(any(), any(), eq(userName));

        ResponseEntity<String> response = controller.updatePassword("321", "123", authentication);

        assertNotNull(response, "Ответ не должен быть NULL");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Код ответа должен быть 200");
        assertEquals("Invalid old password", response.getBody());


    }

}