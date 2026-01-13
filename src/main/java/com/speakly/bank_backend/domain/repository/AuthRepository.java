package com.speakly.bank_backend.domain.repository;


import com.speakly.bank_backend.domain.dto.LoginUserDto;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    Optional<LoginUserDto> findByToken(String token);
    UUID createTokenForUser(Long userId);
    void deleteToken(String token);
}
