package com.speakly.bank_backend.controller.response;

import java.math.BigDecimal;
import java.util.List;

public record AccountDetailsResponse(
        Long id,
        String iban,
        BigDecimal balance,
        List<CreditCardResponse> cards,
        List<BankTransactionResponse> transactions
) {
}
