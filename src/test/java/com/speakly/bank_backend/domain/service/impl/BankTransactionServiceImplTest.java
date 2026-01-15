package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.domain.repository.BankTransactionRepository;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankTransactionServiceImplTest {

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @InjectMocks
    private BankTransactionServiceImpl bankTransactionService;

    private BankTransaction tx;

    @BeforeEach
    void setUp() {
        BankAccount account = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("100.00"), null);
        tx = new BankTransaction(
                10L,
                TransactionOrigin.TRANSFER,
                TransactionType.SUBTRACT,
                null,
                LocalDateTime.parse("2026-01-01T10:15:30"),
                new BigDecimal("5.00"),
                "Compra",
                account
        );
    }

    @Test
    void getById_delegates() {
        when(bankTransactionRepository.findById(10L)).thenReturn(Optional.of(tx));

        Optional<BankTransaction> result = bankTransactionService.getById(10L);

        assertTrue(result.isPresent());
        verify(bankTransactionRepository).findById(10L);
    }

    @Test
    void getAll_delegates() {
        when(bankTransactionRepository.findAll()).thenReturn(List.of(tx));

        List<BankTransaction> result = bankTransactionService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bankTransactionRepository).findAll();
    }

    @Test
    void getAllByBankAccountId_delegates() {
        when(bankTransactionRepository.findByBankAccountId(1L)).thenReturn(List.of(tx));

        List<BankTransaction> result = bankTransactionService.getAllByBankAccountId(1L);

        assertNotNull(result);
        verify(bankTransactionRepository).findByBankAccountId(1L);
    }

    @Nested
    class CreateTests {

        @Test
        void create_setsTransactionDateWhenNull() {
            BankTransaction noDate = new BankTransaction(
                    null,
                    TransactionOrigin.TRANSFER,
                    TransactionType.ADD,
                    null,
                    null,
                    new BigDecimal("1.00"),
                    "Ingreso",
                    tx.getBankAccount()
            );

            when(bankTransactionRepository.save(any(BankTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

            BankTransaction result = bankTransactionService.create(noDate);

            assertNotNull(result.getTransactionDate());
            verify(bankTransactionRepository).save(any(BankTransaction.class));
        }

        @Test
        void create_missingOrigin_throwsIllegalArgumentException() {
            BankTransaction invalid = new BankTransaction(null, null, TransactionType.ADD, null, LocalDateTime.now(), new BigDecimal("1.00"), "x", tx.getBankAccount());

            assertThrows(IllegalArgumentException.class, () -> bankTransactionService.create(invalid));
            verifyNoInteractions(bankTransactionRepository);
        }

        @Test
        void create_creditCardOrigin_withoutCard_throwsIllegalArgumentException() {
            BankTransaction invalid = new BankTransaction(null, TransactionOrigin.CREDIT_CARD, TransactionType.ADD, null, LocalDateTime.now(), new BigDecimal("1.00"), "x", tx.getBankAccount());

            assertThrows(IllegalArgumentException.class, () -> bankTransactionService.create(invalid));
            verifyNoInteractions(bankTransactionRepository);
        }

        @Test
        void create_negativeAmount_throwsIllegalArgumentException() {
            BankTransaction invalid = new BankTransaction(null, TransactionOrigin.TRANSFER, TransactionType.ADD, null, LocalDateTime.now(), new BigDecimal("-1.00"), "x", tx.getBankAccount());

            assertThrows(IllegalArgumentException.class, () -> bankTransactionService.create(invalid));
            verifyNoInteractions(bankTransactionRepository);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void delete_notFound_throwsResourceNotFoundException() {
            when(bankTransactionRepository.findById(10L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> bankTransactionService.deleteById(10L));
            verify(bankTransactionRepository).findById(10L);
            verify(bankTransactionRepository, never()).deleteById(any());
        }

        @Test
        void delete_existing_deletes() {
            when(bankTransactionRepository.findById(10L)).thenReturn(Optional.of(tx));

            bankTransactionService.deleteById(10L);

            verify(bankTransactionRepository).findById(10L);
            verify(bankTransactionRepository).deleteById(10L);
        }
    }
}

