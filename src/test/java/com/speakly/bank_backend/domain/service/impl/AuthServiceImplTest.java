package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.repository.AuthRepository;
import com.speakly.bank_backend.domain.repository.ClientRepository;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginUserDto loginUserDto;
    private Client clientDb;

    @BeforeEach
    void setUp() {
        clientDb = new Client(
                1L,
                "testuser",
                "password123",
                "Test",
                "User",
                "Example",
                "12345678A",
                "apikey1",
                List.of()
        );

        loginUserDto = new LoginUserDto(1L, "testuser");
    }

    @Nested
    class GetUserFromTokenTests {

        @Test
        void getUserFromToken_success() {
            String token = "valid-token";

            when(authRepository.findByToken(token)).thenReturn(Optional.of(loginUserDto));

            LoginUserDto result = authService.getUserFromToken(token);

            assertNotNull(result);
            assertEquals(loginUserDto.id(), result.id());
            assertEquals(loginUserDto.username(), result.username());
            verify(authRepository).findByToken(token);
        }

        @Test
        void getUserFromToken_invalidToken_throwsException() {
            String token = "invalid-token";

            when(authRepository.findByToken(token)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> authService.getUserFromToken(token));

            verify(authRepository).findByToken(token);
        }
    }

    @Nested
    class LoginTests {

        @Test
        void createTokenForUser_success() {
            Client loginRequest = new Client(
                    null,
                    "testuser",
                    "password123",
                    "Test",
                    "User",
                    "Example",
                    "12345678A",
                    "apikey1",
                    List.of()
            );

            UUID token = UUID.randomUUID();

            when(clientRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(clientDb));
            when(authRepository.createTokenForUser(clientDb.getId())).thenReturn(token);

            String result = authService.createTokenForUser(loginRequest);

            assertNotNull(result);
            assertEquals(token.toString(), result);
            verify(clientRepository).findByUsername(loginRequest.getUsername());
            verify(authRepository).createTokenForUser(clientDb.getId());
        }

        @Test
        void createTokenForUser_userNotFound_throwsException() {
            Client loginRequest = new Client(
                    null,
                    "missing",
                    "password123",
                    "Test",
                    "User",
                    "Example",
                    "12345678A",
                    "apikey1",
                    List.of()
            );

            when(clientRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> authService.createTokenForUser(loginRequest));

            verify(clientRepository).findByUsername(loginRequest.getUsername());
            verifyNoInteractions(authRepository);
        }

        @Test
        void createTokenForUser_invalidPassword_throwsException() {
            Client loginRequest = new Client(
                    null,
                    "testuser",
                    "wrongPassword",
                    "Test",
                    "User",
                    "Example",
                    "12345678A",
                    "apikey1",
                    List.of()
            );

            when(clientRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(clientDb));

            assertThrows(BusinessException.class, () -> authService.createTokenForUser(loginRequest));

            verify(clientRepository).findByUsername(loginRequest.getUsername());
            verifyNoInteractions(authRepository);
        }
    }

    @Nested
    class DeleteTokenTests {

        @Test
        void deleteToken_success() {
            String token = "valid-token";

            authService.deleteToken(token);

            verify(authRepository).deleteToken(token);
        }

        @Test
        void deleteToken_nullToken_throwsException() {
            assertThrows(BusinessException.class, () -> authService.deleteToken(null));

            verifyNoInteractions(authRepository);
        }

        @Test
        void deleteToken_blankToken_throwsException() {
            assertThrows(BusinessException.class, () -> authService.deleteToken("  "));

            verifyNoInteractions(authRepository);
        }
    }
}
