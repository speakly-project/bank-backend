package com.speakly.bank_backend.controller.request;

public record LoginRequest(
        String username,
        String password
) {
}
