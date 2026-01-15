package com.speakly.bank_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakly.bank_backend.annotations.AuthenticationInterceptor;
import com.speakly.bank_backend.controller.request.LoginRequest;
import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.service.AuthService;
import com.speakly.bank_backend.domain.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(true);
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(clientService);
    }

    @Test
    void login_ok_when_user_exists() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        Client user = new Client(
                1L,
                "testuser",
                "password",
                "Test",
                "User",
                "Example",
                "12345678A",
                null,
                null
        );

        when(clientService.getByName("testuser")).thenReturn(user);
        when(authService.createTokenForUser(user)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/speakly-bank/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

    }

    @Test
    void login_unauthorized_when_user_not_exists() throws Exception {
        LoginRequest loginRequest = new LoginRequest("noexiste", "password");

        when(clientService.getByName("noexiste")).thenReturn(null);

        mockMvc.perform(post("/api/speakly-bank/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_unauthorized_when_email_is_null() throws Exception {
        LoginRequest loginRequest = new LoginRequest(null, "password");

        when(clientService.getByName(anyString())).thenReturn(null);

        mockMvc.perform(post("/api/speakly-bank/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_ok() throws Exception {
        String token = "Bearer jwt-token";

        doNothing().when(authService).deleteToken(token);

        mockMvc.perform(post("/api/speakly-bank/auth/logout")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(authService).deleteToken(token);
    }
}