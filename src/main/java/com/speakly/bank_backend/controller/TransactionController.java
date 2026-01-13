package com.speakly.bank_backend.controller;


import com.speakly.bank_backend.controller.response.AccountSummaryResponse;
import com.speakly.bank_backend.controller.response.BankTransactionResponse;
import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.service.BankTransactionService;
import com.speakly.bank_backend.mapper.BankTransactionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/speakly-bank/transactions")
public class TransactionController {
    private final BankTransactionService bankTransactionService;

    public TransactionController(BankTransactionService bankTransactionService) {
        this.bankTransactionService = bankTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<BankTransactionResponse>> getTransactionsByBankAccountId(@RequestParam("bankAccountId") Long bankAccountId) {
        List<BankTransaction> accounts = bankTransactionService.getAllByBankAccountId(bankAccountId);

        List<BankTransactionResponse> response = accounts.stream()
                .map(BankTransactionMapper::fromTransactionToTransactionResponse)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
