package com.speakly.bank_backend.persistence.repository;


import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.domain.repository.AuthRepository;
import com.speakly.bank_backend.persistence.dao.AuthDao;

import java.util.Optional;
import java.util.UUID;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthDao authDao;

    public AuthRepositoryImpl(AuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    public Optional<LoginUserDto> findByToken(String token) {
        return authDao.findByToken(token);
    }

    @Override
    public UUID createTokenForUser(Long userId) {
        return authDao.createTokenForUser(userId);
    }

    @Override
    public void deleteToken(String token) {
        authDao.deleteToken(token);
    }
}
