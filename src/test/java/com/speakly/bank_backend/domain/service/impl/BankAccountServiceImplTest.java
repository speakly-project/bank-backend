package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.repository.BankAccountRepository;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("100.00"), null);
    }

    @Test
    void getById_delegatesToRepository() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<BankAccount> result = bankAccountService.getById(1L);

        assertTrue(result.isPresent());
        BankAccount found = result.get();
        assertEquals(account.getIBAN(), found.getIBAN());

        verify(bankAccountRepository).findById(1L);
    }

    @Test
    void getAll_delegatesToRepository() {
        when(bankAccountRepository.findAll()).thenReturn(List.of(account));

        List<BankAccount> result = bankAccountService.getAll();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
        verify(bankAccountRepository).findAll();
    }

    @Test
    void getByIban_notFound_throws() {
        when(bankAccountRepository.findByIban("NO")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bankAccountService.getByIban("NO"));

        verify(bankAccountRepository).findByIban("NO");
    }

    @Nested
    class CreateTests {

        @Test
        void create_valid_saves() {
            when(bankAccountRepository.existsByIban(account.getIBAN())).thenReturn(false);
            when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(inv -> inv.getArgument(0));

            BankAccount result = bankAccountService.create(account);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(account.getIBAN(), result.getIBAN())
            );
            verify(bankAccountRepository).existsByIban(account.getIBAN());
            verify(bankAccountRepository).save(any(BankAccount.class));
        }

        @Test
        void create_duplicateIban_throwsBusinessException() {
            when(bankAccountRepository.existsByIban(account.getIBAN())).thenReturn(true);

            assertThrows(BusinessException.class, () -> bankAccountService.create(account));

            verify(bankAccountRepository).existsByIban(account.getIBAN());
            verify(bankAccountRepository, never()).save(any());
        }

        @Test
        void create_negativeInitialBalance_throwsIllegalArgumentException() {
            BankAccount invalid = new BankAccount(null, "ES00", new BigDecimal("-1.00"), null);
            when(bankAccountRepository.existsByIban(invalid.getIBAN())).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> bankAccountService.create(invalid));

            verify(bankAccountRepository).existsByIban(invalid.getIBAN());
            verify(bankAccountRepository, never()).save(any());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void update_nullId_throwsIllegalArgumentException() {
            BankAccount invalid = new BankAccount(null, "ES00", new BigDecimal("1.00"), null);

            assertThrows(IllegalArgumentException.class, () -> bankAccountService.update(invalid));

            verifyNoInteractions(bankAccountRepository);
        }

        @Test
        void update_notFound_throwsResourceNotFoundException() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> bankAccountService.update(account));

            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository, never()).save(any());
        }

        @Test
        void update_existing_saves() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(inv -> inv.getArgument(0));

            BankAccount result = bankAccountService.update(account);

            assertNotNull(result);
            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository).save(any(BankAccount.class));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void deleteById_notFound_throws() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> bankAccountService.deleteById(1L));

            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository, never()).deleteById(any());
        }

        @Test
        void deleteById_existing_deletes() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

            bankAccountService.deleteById(1L);

            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository).deleteById(1L);
        }
    }

    @Nested
    class BalanceOpsTests {

        @Test
        void addBalance_positiveAmount_updatesBalance() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(inv -> inv.getArgument(0));

            BankAccount result = bankAccountService.addBalance(1L, new BigDecimal("50.00"));

            assertEquals(new BigDecimal("150.00"), result.getBalance());
            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository).save(any(BankAccount.class));
        }

        @Test
        void addBalance_zeroAmount_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> bankAccountService.addBalance(1L, BigDecimal.ZERO));

            verifyNoInteractions(bankAccountRepository);
        }

        @Test
        void subtractBalance_insufficient_throwsBusinessException() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

            assertThrows(BusinessException.class,
                    () -> bankAccountService.subtractBalance(1L, new BigDecimal("200.00")));

            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository, never()).save(any());
        }

        @Test
        void subtractBalance_positiveAmount_updatesBalance() {
            when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(inv -> inv.getArgument(0));

            BankAccount result = bankAccountService.subtractBalance(1L, new BigDecimal("40.00"));

            assertEquals(new BigDecimal("60.00"), result.getBalance());
            verify(bankAccountRepository).findById(1L);
            verify(bankAccountRepository).save(any(BankAccount.class));
        }

        @Test
        void subtractBalance_negativeAmount_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> bankAccountService.subtractBalance(1L, new BigDecimal("-1.00")));

            verifyNoInteractions(bankAccountRepository);
        }

        @Test
        void subtractBalance_accountNotFound_throwsResourceNotFoundException() {
            when(bankAccountRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> bankAccountService.subtractBalance(999L, new BigDecimal("1.00")));

            verify(bankAccountRepository).findById(999L);
        }
    }
}
