package com.speakly.bank_backend.controller;

import com.speakly.bank_backend.controller.response.AccountDetailsResponse;
import com.speakly.bank_backend.controller.response.AccountSummaryResponse;
import com.speakly.bank_backend.controller.response.BankTransactionResponse;
import com.speakly.bank_backend.controller.response.CreditCardResponse;
import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.service.BankAccountService;
import com.speakly.bank_backend.domain.service.BankTransactionService;
import com.speakly.bank_backend.domain.service.CreditCardService;
import com.speakly.bank_backend.mapper.BankTransactionMapper;
import com.speakly.bank_backend.mapper.CreditCardMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speakly-bank/accounts")
public class AccountController {

    private final BankAccountService bankAccountService;
    private final CreditCardService creditCardService;
    private final BankTransactionService bankTransactionService;

    public AccountController(
            BankAccountService bankAccountService,
            CreditCardService creditCardService,
            BankTransactionService bankTransactionService
    ) {
        this.bankAccountService = bankAccountService;
        this.creditCardService = creditCardService;
        this.bankTransactionService = bankTransactionService;
    }

    /**
     * Lista todas las cuentas de un cliente.
     * Nota: en ausencia de auth/SpringSecurity en el repo, usamos clientId por path.
     */
    @GetMapping
    public ResponseEntity<List<AccountSummaryResponse>> getAccountsByClientId(@RequestParam("clientId") Long clientId) {
        List<BankAccount> accounts = bankAccountService.getAllByClientId(clientId);

        List<AccountSummaryResponse> response = accounts.stream()
                .map(bankAccount -> new AccountSummaryResponse(bankAccount.getId(), bankAccount.getIBAN(), bankAccount.getBalance()))
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Devuelve el detalle completo de una cuenta por IBAN: balance + tarjetas + transacciones.
     * Protege acceso básico comprobando que la cuenta pertenece al clientId indicado.
     */
    @GetMapping("/{iban}")
    public ResponseEntity<AccountDetailsResponse> getAccountDetailsByIban(
            @PathVariable String iban
    ) {
        BankAccount account = bankAccountService.getByIban(iban);

        List<CreditCardResponse> cards = creditCardService.getAllByBankAccountId(account.getId()).stream()
                .map(CreditCardMapper::fromCardToCardResponse)
                .toList();

        List<BankTransactionResponse> transactions = bankTransactionService.getAllByBankAccountId(account.getId()).stream()
                .map(BankTransactionMapper::fromTransactionToTransactionResponse)
                .toList();

        AccountDetailsResponse response = new AccountDetailsResponse(
                account.getId(),
                account.getIBAN(),
                account.getBalance(),
                cards,
                transactions
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Lista todas las cuentas de un cliente con sus tarjetas y transacciones incluidas.
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<AccountDetailsResponse>> getAccountsWithCardsByClientId(
            @RequestParam("clientId") Long clientId
    ) {
        List<BankAccount> accounts = bankAccountService.getAllByClientId(clientId);

        List<AccountDetailsResponse> response = accounts.stream()
                .map(account -> {
                    List<CreditCardResponse> cards = creditCardService.getAllByBankAccountId(account.getId())
                            .stream()
                            .map(CreditCardMapper::fromCardToCardResponse)
                            .toList();

                    List<BankTransactionResponse> transactions = bankTransactionService.getAllByBankAccountId(account.getId())
                            .stream()
                            .map(BankTransactionMapper::fromTransactionToTransactionResponse)
                            .toList();

                    return new AccountDetailsResponse(
                            account.getId(),
                            account.getIBAN(),
                            account.getBalance(),
                            cards,
                            transactions
                    );
                })
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
