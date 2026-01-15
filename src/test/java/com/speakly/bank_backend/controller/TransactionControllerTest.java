package com.speakly.bank_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakly.bank_backend.annotations.AuthenticationInterceptor;
import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.domain.service.BankTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BankTransactionService bankTransactionService;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(true);
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(bankTransactionService);
    }

    @Test
    void getTransactionsByBankAccountId_ok() throws Exception {
        BankAccount account = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1500.50"), null);

        BankTransaction tx1 = new BankTransaction(
                100L,
                TransactionOrigin.TRANSFER,
                TransactionType.SUBTRACT,
                null,
                LocalDateTime.parse("2026-01-01T10:15:30"),
                new BigDecimal("50.00"),
                "Compra",
                account
        );

        BankTransaction tx2 = new BankTransaction(
                101L,
                TransactionOrigin.DOM,
                TransactionType.SUBTRACT,
                null,
                LocalDateTime.parse("2026-01-02T11:00:00"),
                new BigDecimal("10.00"),
                "Recibo",
                account
        );

        when(bankTransactionService.getAllByBankAccountId(1L)).thenReturn(List.of(tx1, tx2));

        mockMvc.perform(get("/api/speakly-bank/transactions")
                        .param("bankAccountId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].origin").value("TRANSFER"))
                .andExpect(jsonPath("$[0].type").value("SUBTRACT"))
                .andExpect(jsonPath("$[0].originCardNumber").doesNotExist())
                .andExpect(jsonPath("$[0].transactionDate").value("2026-01-01T10:15:30"))
                .andExpect(jsonPath("$[0].amount").value(50.00))
                .andExpect(jsonPath("$[0].description").value("Compra"))
                .andExpect(jsonPath("$[1].id").value(101));

        Mockito.verify(bankTransactionService).getAllByBankAccountId(1L);
    }

    @Test
    void getTransactionsByBankAccountId_ok_emptyList() throws Exception {
        when(bankTransactionService.getAllByBankAccountId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/speakly-bank/transactions")
                        .param("bankAccountId", "99"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(bankTransactionService).getAllByBankAccountId(99L);
    }
}
