package com.speakly.bank_backend.domain.service;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;

public interface UserService {
    Page<UserDto> getAll(int pageNumber, int pageSize);
    UserDto getById(Long id);
    UserDto getByEmail(String email);
    UserDto getByUsername(String username);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user);
    void delete(Long id);
}
