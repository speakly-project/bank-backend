package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.repository.AuthRepository;
import com.speakly.bank_backend.domain.repository.ClientRepository;
import com.speakly.bank_backend.domain.service.AuthService;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

public class AuthServiceImpl implements AuthService {
    private final ClientRepository userRepository;
    private final AuthRepository authRepository;

    public AuthServiceImpl(ClientRepository userRepository, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }


    @Override
    public LoginUserDto getUserFromToken(String token) {
        return authRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
    }

    @Override
    @Transactional
    public String createTokenForUser(Client user) {
        Client userDb = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User with username " + user.getUsername() + " not found"));

        if (userDb.getPassword().equals(user.getPassword())) {
            throw new BusinessException("Invalid password");
        }
        return authRepository.createTokenForUser(userDb.getId()).toString();
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        if (token == null || token.isBlank()) {
            throw new BusinessException("Token cannot be null");
        }
        authRepository.deleteToken(token);

    }
}
