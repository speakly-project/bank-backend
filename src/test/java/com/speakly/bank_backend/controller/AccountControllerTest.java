package com.speakly.bank_backend.controller;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.model.CreditCard;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.domain.repository.BankTransactionRepository;
import com.speakly.bank_backend.domain.repository.CreditCardRepository;
import com.speakly.bank_backend.domain.service.BankAccountService;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankAccountService bankAccountService;

    @MockitoBean
    private CreditCardRepository creditCardRepository;

    @MockitoBean
    private BankTransactionRepository bankTransactionRepository;

    private static Client client10() {
        return new Client(
                10L,
                "juan.perez",
                "secret",
                "Juan",
                "Pérez",
                "García",
                "12345678A",
                "apikey1",
                List.of()
        );
    }

    @Test
    void shouldListAccountsByClientId() throws Exception {
        Client client = client10();
        BankAccount a1 = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1500.50"), client);
        BankAccount a2 = new BankAccount(2L, "ES7921000813610123456789", new BigDecimal("25.00"), client);

        when(bankAccountService.getAllByClientId(10L)).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/speakly-bank/accounts/client/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].iban").value("ES9121000418450200051332"))
                .andExpect(jsonPath("$[0].balance").value(1500.50));
    }

    @Test
    void shouldReturnAccountDetailsByIban() throws Exception {
        Client client = client10();
        BankAccount account = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1500.50"), client);

        CreditCard card = new CreditCard(8L, "4532015112830366", YearMonth.parse("2027-12"), "123", "Juan Pérez García", account);
        BankTransaction tx = new BankTransaction(
                100L,
                TransactionOrigin.CREDIT_CARD,
                TransactionType.SUBTRACT,
                card,
                LocalDateTime.parse("2026-01-01T10:15:30"),
                new BigDecimal("50.00"),
                "Compra",
                account
        );

        when(bankAccountService.getByIban("ES9121000418450200051332")).thenReturn(account);
        when(creditCardRepository.findByBankAccountId(1L)).thenReturn(List.of(card));
        when(bankTransactionRepository.findByBankAccountId(1L)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/speakly-bank/accounts/ES9121000418450200051332/details")
                        .param("clientId", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.iban").value("ES9121000418450200051332"))
                .andExpect(jsonPath("$.balance").value(1500.50))
                .andExpect(jsonPath("$.cards[0].cardNumber").value("4532015112830366"))
                .andExpect(jsonPath("$.transactions[0].id").value(100));
    }

    @Test
    void shouldReturnNotFoundWhenIbanDoesNotExist() throws Exception {
        when(bankAccountService.getByIban("ES0000000000000000000000"))
                .thenThrow(new ResourceNotFoundException("Bank account not found with IBAN: ES0000000000000000000000"));

        mockMvc.perform(get("/api/speakly-bank/accounts/ES0000000000000000000000/details")
                        .param("clientId", "10"))
                .andExpect(status().isNotFound());
    }
}
