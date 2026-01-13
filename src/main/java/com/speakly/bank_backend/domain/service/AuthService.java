package com.speakly.bank_backend.domain.service;


import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.domain.model.Client;

public interface AuthService {
    LoginUserDto getUserFromToken(String token);
    String createTokenForUser(Client user);
    void deleteToken(String token);

}
