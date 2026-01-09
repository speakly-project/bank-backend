package com.speakly.bank_backend.domain.dto;

import java.time.LocalDateTime;


public record CardPaymentDto(
        AuthorizationDto authorization,
        OriginDto origin,
        DestinationDto destination,
        PaymentDto payment,
        LocalDateTime createdAt
){
    public CardPaymentDto {
        createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
