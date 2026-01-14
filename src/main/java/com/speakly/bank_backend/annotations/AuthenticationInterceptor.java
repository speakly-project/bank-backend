package com.speakly.bank_backend.annotations;


import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        boolean requiresAuth =
                handlerMethod.hasMethodAnnotation(Authenticated.class)
                        || handlerMethod.getBeanType().isAnnotationPresent(Authenticated.class);


        if (!requiresAuth) {
            return true;
        }

        LoginUserDto user = (LoginUserDto) request.getAttribute("user");

        if (user == null) {
            throw new UnauthorizedException("Token de autenticación requerido o inválido");
        }

        if (user.id() == null) {
            throw new UnauthorizedException("Token de autenticación inválido o corrupto");
        }

        return true;
    }
}
