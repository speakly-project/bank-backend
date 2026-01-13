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
import com.speakly.bank_backend.domain.service.ClientService;
import com.speakly.bank_backend.domain.service.CreditCardService;
import com.speakly.bank_backend.domain.usecase.CardTransactionUseCase;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;

public class CardTransactionUseCaseImpl implements CardTransactionUseCase {

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;
    private final ClientService clientService;

    public CardTransactionUseCaseImpl(CreditCardService creditCardService,
                                      BankAccountService bankAccountService,
                                      BankTransactionService bankTransactionService,
                                      ClientService clientService) {
        this.creditCardService = creditCardService;
        this.bankAccountService = bankAccountService;
        this.bankTransactionService = bankTransactionService;
        this.clientService = clientService;
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

        if (creditCard.getBankAccount().getIBAN() == null){
            throw new BusinessException("Credit card is not associated with any bank account IBAN");
        }
        BankAccount destinationAccount = bankAccountService.getByIban(iban);

        BankAccount cardAccount = creditCard.getBankAccount();
        if (cardAccount == null) {
            throw new BusinessException("Credit card is not associated with any bank account");
        }

        // Validaciones de seguridad y reglas de negocio
        validateCreditCard(creditCard, request);
        validateDestinationAccount(destinationAccount, request);
        validateSourceAccount(cardAccount, amount);
        validateNoSelfTransfer(cardAccount, destinationAccount);

        TransactionType transactionType = determineTransactionType();
        BigDecimal absoluteAmount = amount.abs();

        // Substraer del origen (cuenta de la tarjeta) y agregar al destino
        bankAccountService.subtractBalance(cardAccount.getId(), absoluteAmount);
        bankAccountService.addBalance(destinationAccount.getId(), absoluteAmount);

        BankTransaction subtractTransaction = new BankTransaction(
                null,
                TransactionOrigin.CREDIT_CARD,
                TransactionType.SUBTRACT,
                creditCard,
                null,
                absoluteAmount,
                description,
                cardAccount
        );

        BankTransaction addTransaction = new BankTransaction(
                null,
                TransactionOrigin.TRANSFER,
                TransactionType.ADD,
                null,
                null,
                absoluteAmount,
                description,
                destinationAccount
        );

        bankTransactionService.create(subtractTransaction);
        bankTransactionService.create(addTransaction);

        return new CardPaymentDto(
                request.authorization(),
                request.origin(),
                request.destination(),
                request.payment(),
                request.createdAt()
        );
    }

    private TransactionType determineTransactionType() {
        // Como el amount ya fue validado como positivo, siempre es SUBTRACT del origen
        return TransactionType.SUBTRACT;
    }

    private void validateRequest(CardPaymentRequest request) {
        // Validar autenticación
        String authUsername = request.authorization().username();
        clientService.getByApiKey(request.authorization().apiKey())
                .filter(c -> c.getUsername() != null && c.getUsername().equals(authUsername))
                .orElseThrow(() -> new BusinessException("API key inválida o no relacionada al usuario"));

        // Validar datos de origen (tarjeta)
        if (request.origin() == null) {
            throw new IllegalArgumentException("Origin data is required");
        }
        if (request.origin().cardNumber() == null || request.origin().cardNumber().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (request.origin().expiryDate() == null || request.origin().expiryDate().isEmpty()) {
            throw new IllegalArgumentException("Card expiry date is required");
        }
        if (request.origin().cvv() == null || request.origin().cvv().isEmpty()) {
            throw new IllegalArgumentException("Card CVV is required");
        }
        if (request.origin().fullName() == null || request.origin().fullName().isEmpty()) {
            throw new IllegalArgumentException("Card holder name is required");
        }

        // Validar datos de destino (IBAN)
        if (request.destination() == null || request.destination().IBAN() == null) {
            throw new IllegalArgumentException("Destination IBAN is required");
        }
        validateIbanFormat(request.destination().IBAN());

        // Validar datos de pago
        if (request.payment() == null) {
            throw new IllegalArgumentException("Payment data is required");
        }

        // El importe debe ser positivo
        if (request.payment().amount() == null || request.payment().amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        // El concepto debe tener al menos 3 letras
        if (request.payment().description() == null || request.payment().description().trim().length() < 3) {
            throw new IllegalArgumentException("Payment description must have at least 3 characters");
        }
    }

    private void validateIbanFormat(String iban) {
        // El IBAN debe empezar por ES
        if (!iban.startsWith("ES")) {
            throw new IllegalArgumentException("IBAN must start with ES");
        }

        // Validar formato básico de IBAN español (ES + 22 dígitos)
        String ibanClean = iban.replaceAll("\\s+", "");
        if (!ibanClean.matches("ES\\d{22}")) {
            throw new IllegalArgumentException("Invalid IBAN format. Spanish IBAN must be ES followed by 22 digits");
        }
    }


    private void validateCreditCard(CreditCard creditCard, CardPaymentRequest request) {
        // Validar que la tarjeta no esté expirada
        if (creditCard.getExpirationDate() != null && creditCard.getExpirationDate().isBefore(YearMonth.now())) {
            throw new BusinessException("Credit card is expired");
        }

        // Todos los datos de la tarjeta deben coincidir
        String requestExpiryDate = request.origin().expiryDate();
        String cardExpiryDate = creditCard.getExpirationDate().toString();
        if (!cardExpiryDate.equals(requestExpiryDate)) {
            throw new BusinessException("Credit card expiry date does not match");
        }

        if (!creditCard.getCvc().equals(request.origin().cvv())) {
            throw new BusinessException("Credit card CVV does not match");
        }

        if (!creditCard.getFullName().equals(request.origin().fullName())) {
            throw new BusinessException("Credit card holder name does not match");
        }
    }

    private void validateDestinationAccount(BankAccount account, CardPaymentRequest req) {
        // Validar que la cuenta destino existe y tiene IBAN válido
        if (account.getIBAN() == null || account.getIBAN().isEmpty()) {
            throw new BusinessException("Destination account IBAN is missing");
        }

        // Validar que la cuenta destino tiene cliente asociado
        if (account.getClient() == null) {
            throw new BusinessException("Destination account is not associated with any client");
        }

        // Validar que el IBAN de destino pertenece a una cuenta del cliente autenticado
        clientService.getByApiKey(req.authorization().apiKey())
                .filter(client -> account.getClient().getId().equals(client.getId()))
                .orElseThrow(() -> new BusinessException("Destination account does not belong to the authenticated client"));
    }

    private void validateSourceAccount(BankAccount account, BigDecimal amount) {
        // Debe haber suficiente dinero en la cuenta de la tarjeta asociada
        if (account.getBalance() == null) {
            throw new BusinessException("Source account balance is not available");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient balance in source account");
        }
    }

    private void validateNoSelfTransfer(BankAccount sourceAccount, BankAccount destinationAccount) {
        // cuenta destino y origen no sean la misma
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new BusinessException("Cannot transfer funds to the same account");
        }
    }

}
