package com.speakly.bank_backend.usecase;

import com.speakly.bank_backend.controller.request.CardPaymentRequest;
import com.speakly.bank_backend.domain.dto.CardPaymentDto;
import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.CreditCard;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.domain.service.BankAccountService;
import com.speakly.bank_backend.domain.service.BankTransactionService;
import com.speakly.bank_backend.domain.service.CreditCardService;
import com.speakly.bank_backend.domain.usecase.CardTransactionUseCase;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

public class CardTransactionUseCaseImpl implements CardTransactionUseCase {

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;

    public CardTransactionUseCaseImpl(CreditCardService creditCardService,
                                     BankAccountService bankAccountService,
                                     BankTransactionService bankTransactionService) {
        this.creditCardService = creditCardService;
        this.bankAccountService = bankAccountService;
        this.bankTransactionService = bankTransactionService;
    }

    @Override
    @Transactional
    public CardPaymentDto processCardPayment(CardPaymentRequest request) {
        validateRequest(request);

        String cardNumber = request.origin().cardNumber();
        String iban = request.destination().IBAN();
        BigDecimal amount = request.payment().amount();
        String description = request.payment().description();

        CreditCard creditCard = creditCardService.getByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Credit card not found: " + cardNumber));

        BankAccount destinationAccount = bankAccountService.getByIban(iban)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found: " + iban));

        BankAccount cardAccount = creditCard.getBankAccount();
        if (cardAccount == null) {
            throw new BusinessException("Credit card is not associated with any bank account");
        }


        TransactionType transactionType = determineTransactionType(amount);
        BigDecimal absoluteAmount = amount.abs();

        if (transactionType == TransactionType.ADD) {
            bankAccountService.addBalance(destinationAccount.getId(), absoluteAmount);
        } else {
            bankAccountService.subtractBalance(cardAccount.getId(), absoluteAmount);
        }

        BankTransaction transaction = new BankTransaction(
                null,
                TransactionOrigin.CREDIT_CARD,
                transactionType,
                creditCard,
                null,
                absoluteAmount,
                description,
                destinationAccount
        );

        bankTransactionService.create(transaction);

        return new CardPaymentDto(
                request.authorization(),
                request.origin(),
                request.destination(),
                request.payment(),
                request.createdAt()
        );
    }

    private void validateRequest(CardPaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        if (request.authorization() == null) {
            throw new IllegalArgumentException("Authorization data is required");
        }
        if (request.origin() == null || request.origin().cardNumber() == null) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (request.destination() == null || request.destination().IBAN() == null) {
            throw new IllegalArgumentException("Destination IBAN is required");
        }
        if (request.payment() == null) {
            throw new IllegalArgumentException("Payment data is required");
        }
        if (request.payment().amount() == null || request.payment().amount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Payment amount must be non-zero");
        }
    }

    private TransactionType determineTransactionType(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0 ? TransactionType.ADD : TransactionType.SUBTRACT;
    }
}



