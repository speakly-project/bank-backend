package com.speakly.bank_backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginUserDto(
        Long id,
        @NotBlank(message = "Username cannot be blank")
        String username
) {
}