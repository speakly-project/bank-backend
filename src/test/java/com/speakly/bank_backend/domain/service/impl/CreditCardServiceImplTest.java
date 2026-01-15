package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.model.CreditCard;
import com.speakly.bank_backend.domain.repository.CreditCardRepository;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    private CreditCard card;

    @BeforeEach
    void setUp() {
        BankAccount account = new BankAccount(1L, "ES9121000418450200051332", null, null);
        card = new CreditCard(1L, "4532015112830366", YearMonth.now().plusMonths(1), "123", "Juan Pérez García", account);
    }

    @Test
    void getAll_delegates() {
        when(creditCardRepository.findAll()).thenReturn(List.of(card));

        List<CreditCard> result = creditCardService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(creditCardRepository).findAll();
    }

    @Test
    void getByCardNumber_delegates() {
        when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));

        Optional<CreditCard> result = creditCardService.getByCardNumber(card.getCardNumber());

        assertTrue(result.isPresent());
        verify(creditCardRepository).findByCardNumber(card.getCardNumber());
    }

    @Test
    void getAllByBankAccountId_delegates() {
        when(creditCardRepository.findByBankAccountId(1L)).thenReturn(List.of(card));

        List<CreditCard> result = creditCardService.getAllByBankAccountId(1L);

        assertNotNull(result);
        verify(creditCardRepository).findByBankAccountId(1L);
    }

    @Nested
    class CreateTests {

        @Test
        void create_valid_saves() {
            when(creditCardRepository.existsByCardNumber(card.getCardNumber())).thenReturn(false);
            when(creditCardRepository.save(any(CreditCard.class))).thenAnswer(inv -> inv.getArgument(0));

            CreditCard result = creditCardService.create(card);

            assertNotNull(result);
            verify(creditCardRepository).existsByCardNumber(card.getCardNumber());
            verify(creditCardRepository).save(any(CreditCard.class));
        }

        @Test
        void create_duplicateCardNumber_throwsIllegalArgumentException() {
            when(creditCardRepository.existsByCardNumber(card.getCardNumber())).thenReturn(true);

            assertThrows(IllegalArgumentException.class, () -> creditCardService.create(card));
            verify(creditCardRepository).existsByCardNumber(card.getCardNumber());
            verify(creditCardRepository, never()).save(any());
        }

        @Test
        void create_expired_throwsIllegalArgumentException() {
            CreditCard expired = new CreditCard(1L, "4532015112830366", YearMonth.now().minusMonths(1), "123", "Juan Pérez García", card.getBankAccount());
            when(creditCardRepository.existsByCardNumber(expired.getCardNumber())).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> creditCardService.create(expired));
            verify(creditCardRepository).existsByCardNumber(expired.getCardNumber());
            verify(creditCardRepository, never()).save(any());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void update_notFound_throwsResourceNotFoundException() {
            when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> creditCardService.update(card));
            verify(creditCardRepository).findByCardNumber(card.getCardNumber());
            verify(creditCardRepository, never()).save(any());
        }

        @Test
        void update_existing_saves() {
            when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));
            when(creditCardRepository.save(any(CreditCard.class))).thenAnswer(inv -> inv.getArgument(0));

            CreditCard result = creditCardService.update(card);

            assertNotNull(result);
            verify(creditCardRepository).findByCardNumber(card.getCardNumber());
            verify(creditCardRepository).save(any(CreditCard.class));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void delete_notFound_throwsResourceNotFoundException() {
            when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> creditCardService.deleteByCardNumber(card.getCardNumber()));
            verify(creditCardRepository).findByCardNumber(card.getCardNumber());
            verify(creditCardRepository, never()).deleteByCardNumber(any());
        }

        @Test
        void delete_existing_deletes() {
            when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));

            creditCardService.deleteByCardNumber(card.getCardNumber());

            verify(creditCardRepository).findByCardNumber(card.getCardNumber());
            verify(creditCardRepository).deleteByCardNumber(card.getCardNumber());
        }
    }
}

