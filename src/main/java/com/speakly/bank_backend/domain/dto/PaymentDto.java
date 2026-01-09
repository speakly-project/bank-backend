package com.speakly.bank_backend.domain.dto;

import java.math.BigDecimal;

public record PaymentDto(
        BigDecimal amount,
        String description
) {

}
