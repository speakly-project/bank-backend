package com.speakly.bank_backend.controller.response;

import java.math.BigDecimal;

public record AccountSummaryResponse(
        Long id,
        String iban,
        BigDecimal balance
) {
}
