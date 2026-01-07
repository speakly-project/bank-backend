package com.speakly.bank_backend.persistence.dao;

import es.speakly.store_backend.domain.dto.LoginUserDto;

import java.util.Optional;
import java.util.UUID;

public interface AuthDao {
    Optional<LoginUserDto> findByToken(String token);
    UUID createTokenForUser(Long userId);
    void deleteToken(String token);
    Long count();
}
