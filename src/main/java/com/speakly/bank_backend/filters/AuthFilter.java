package com.speakly.bank_backend.filters;

import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.domain.service.AuthService;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = obtenerToken(request);

        if (token != null) {
            try {
                LoginUserDto user = authService.getUserFromToken(token);

                if (user != null) {
                    request.setAttribute("user", user);
                }
            } catch (ResourceNotFoundException e) {
                request.setAttribute("user", null);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String obtenerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return request.getParameter("token");
    }
}

