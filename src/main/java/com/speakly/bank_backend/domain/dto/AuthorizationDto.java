package com.speakly.bank_backend.domain.dto;

public record AuthorizationDto(
        String username,
        String apiKey
) {
}
