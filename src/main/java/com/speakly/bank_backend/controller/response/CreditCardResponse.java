package com.speakly.bank_backend.controller.response;

import java.time.YearMonth;

public record CreditCardResponse(
        String cardNumber,
        YearMonth expirationDate,
        String cvv,
        String fullName
) {
}

