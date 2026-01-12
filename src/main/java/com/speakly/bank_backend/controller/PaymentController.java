package com.speakly.bank_backend.controller;

import com.speakly.bank_backend.controller.request.CardPaymentRequest;
import com.speakly.bank_backend.controller.response.CardPaymentResponse;
import com.speakly.bank_backend.domain.dto.CardPaymentDto;
import com.speakly.bank_backend.domain.usecase.CardTransactionUseCase;
import com.speakly.bank_backend.exceptions.DtoValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/speakly-bank/payments")
public class PaymentController {

    private final CardTransactionUseCase cardTransactionUseCase;

    public PaymentController(CardTransactionUseCase cardTransactionUseCase) {
        this.cardTransactionUseCase = cardTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<CardPaymentResponse> processCardPayment(@RequestBody CardPaymentRequest cardPaymentRequest) {

       DtoValidator.validate(cardPaymentRequest);
        CardPaymentDto result = cardTransactionUseCase.processCardPayment(cardPaymentRequest);

        CardPaymentResponse response = new CardPaymentResponse(
                result.origin(),
                result.destination(),
                result.payment(),
                "SUCCESS");

        return ResponseEntity.ok(response);
    }
}
