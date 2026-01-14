package com.speakly.bank_backend.controller.response;

import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankTransactionResponse(
        Long id,
        TransactionOrigin origin,
        TransactionType type,
        String originCardNumber,
        LocalDateTime transactionDate,
        BigDecimal amount,
        String description
) {
}
