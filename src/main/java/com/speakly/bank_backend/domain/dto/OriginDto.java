package com.speakly.bank_backend.domain.dto;

public record OriginDto (
        String cardNumber,
        String expiryDate,
        String cvv,
        String fullName
) {
}
