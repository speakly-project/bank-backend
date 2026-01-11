package com.speakly.bank_backend.controller.response;

import com.speakly.bank_backend.domain.dto.AuthorizationDto;
import com.speakly.bank_backend.domain.dto.DestinationDto;
import com.speakly.bank_backend.domain.dto.OriginDto;
import com.speakly.bank_backend.domain.dto.PaymentDto;

import java.time.LocalDateTime;

public record CardPaymentResponse(
        OriginDto origin,
        DestinationDto destination,
        PaymentDto payment,
        String status
) {
}
