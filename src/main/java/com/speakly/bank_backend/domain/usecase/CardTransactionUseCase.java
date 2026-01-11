package com.speakly.bank_backend.domain.usecase;

import com.speakly.bank_backend.controller.request.CardPaymentRequest;
import com.speakly.bank_backend.domain.dto.CardPaymentDto;

public interface CardTransactionUseCase {
    CardPaymentDto processCardPayment(CardPaymentRequest request);
}
