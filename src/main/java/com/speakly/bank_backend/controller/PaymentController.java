package com.speakly.bank_backend.controller;

import com.speakly.bank_backend.controller.request.CardPaymentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/speakly-bank/payments")
public class PaymentController {
    public PaymentController() {
    }
    @PostMapping
    public ResponseEntity<CardPaymentRequest> processCardPayment(@RequestBody CardPaymentRequest cardPaymentRequest) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
