package com.speakly.bank_backend.persistence.dao.jpa.impl;

import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import com.speakly.bank_backend.persistence.dao.impl.AuthDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;
import com.speakly.bank_backend.persistence.dao.impl.entity.SessionJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AuthDaoImpl.class)
class AuthDaoImplTest {

    @Autowired
    private AuthDaoImpl authDao;

    @PersistenceContext
    private EntityManager entityManager;

    private ClientJpaEntity clientJpaEntity;

    @BeforeEach
    void setUp() {
        String unique = java.util.UUID.randomUUID().toString().substring(0, 8);

        clientJpaEntity = new ClientJpaEntity(
                null,
                "testuser_" + unique,
                "encrypted-password",
                "Test",
                "User",
                "Example",
                "12345678" + unique.charAt(0) + "A",
                "apikey_" + unique
        );
        entityManager.persist(clientJpaEntity);
        entityManager.flush();
    }

    @Test
    void findByToken_success() {
        String token = "valid-token";

        SessionJpaEntity session = new SessionJpaEntity(token, clientJpaEntity, LocalDateTime.now());
        entityManager.persist(session);
        entityManager.flush();

        Optional<LoginUserDto> result = authDao.findByToken(token);

        assertTrue(result.isPresent());
        LoginUserDto dto = result.get();

        assertAll(
                () -> assertEquals(clientJpaEntity.getId(), dto.id()),
                () -> assertEquals(clientJpaEntity.getUsername(), dto.username())
        );
    }

    @Test
    void findByToken_notFound() {
        Optional<LoginUserDto> result = authDao.findByToken("invalid-token");
        assertTrue(result.isEmpty());
    }

    @Test
    void createTokenForUser_success() {
        UUID token = authDao.createTokenForUser(clientJpaEntity.getId());

        Long count = authDao.count();

        assertAll(
                () -> assertNotNull(token),
                () -> assertEquals(1L, count)
        );
    }

    @Test
    void createTokenForUser_userNotFound_throwsException() {
        assertThrows(ResourceNotFoundException.class,
                () -> authDao.createTokenForUser(999L));
    }

    @Test
    void deleteToken_success() {
        String token = "token-to-delete";

        SessionJpaEntity session = new SessionJpaEntity(token, clientJpaEntity, LocalDateTime.now());
        entityManager.persist(session);
        entityManager.flush();

        authDao.deleteToken(token);

        Long count = authDao.count();
        assertEquals(0L, count);
    }
}
