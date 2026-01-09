package com.speakly.bank_backend.controller.request;

import com.speakly.bank_backend.domain.dto.AuthorizationDto;
import com.speakly.bank_backend.domain.dto.DestinationDto;
import com.speakly.bank_backend.domain.dto.OriginDto;
import com.speakly.bank_backend.domain.dto.PaymentDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CardPaymentRequest(
    AuthorizationDto authorization,
    OriginDto origin,
    DestinationDto destination,
    PaymentDto payment,
    LocalDateTime createdAt
){
    public CardPaymentRequest {
        createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
