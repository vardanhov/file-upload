package com.example.uploadfile.security;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Slf4j
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    //FIXME в отличии от стандартной реализации не отображает оишбку на форме запроса логина/пароля
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        log.error(MarkerFactory.getMarker("user"), "[Authentication Failure]  | username={} | message={} | type={} ",
                Arrays.stream(request.getParameterMap().get("username")).findFirst().get(),
                exception.getMessage(),
                "UserEvent");
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
