package com.speakly.bank_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakly.bank_backend.annotations.AuthenticationInterceptor;
import com.speakly.bank_backend.controller.request.CardPaymentRequest;
import com.speakly.bank_backend.domain.dto.*;
import com.speakly.bank_backend.domain.usecase.CardTransactionUseCase;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardTransactionUseCase cardTransactionUseCase;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    private CardPaymentRequest paymentRequest;
    private CardPaymentDto paymentDto;

    @BeforeEach
    void setUpInterceptor() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(true);
    }

    @BeforeEach
    void setUp() {
        AuthorizationDto authorization = new AuthorizationDto("juan.perez", "apikey1");
        OriginDto origin = new OriginDto("4532015112830366", "2027-12", "123", "Juan Pérez García");
        DestinationDto destination = new DestinationDto("ES9121000418450200051332");
        PaymentDto payment = new PaymentDto(new BigDecimal("50.00"), "Compra en tienda");

        paymentRequest = new CardPaymentRequest(
                authorization,
                origin,
                destination,
                payment,
                LocalDateTime.now()
        );

        paymentDto = new CardPaymentDto(
                authorization,
                origin,
                destination,
                payment,
                LocalDateTime.now()
        );
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(cardTransactionUseCase);
    }

    @Nested
    class SuccessTests {

        @Test
        void shouldProcessCardPaymentSuccessfully() throws Exception {
            Mockito.when(cardTransactionUseCase.processCardPayment(any()))
                    .thenReturn(paymentDto);

            String requestJson = objectMapper.writeValueAsString(paymentRequest);

            mockMvc.perform(post("/api/speakly-bank/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.status").value("SUCCESS"))
                    .andExpect(jsonPath("$.origin.cardNumber").value("4532015112830366"))
                    .andExpect(jsonPath("$.destination.IBAN").value("ES9121000418450200051332"))
                    .andExpect(jsonPath("$.payment.amount").value(50.00))
                    .andExpect(jsonPath("$.payment.description").value("Compra en tienda"));

            verify(cardTransactionUseCase).processCardPayment(any());
        }

        @Test
        void shouldProcessCardPaymentWithNegativeAmount() throws Exception {
            PaymentDto negativePayment = new PaymentDto(new BigDecimal("-100.00"), "Reembolso");
            CardPaymentRequest negativeRequest = new CardPaymentRequest(
                    paymentRequest.authorization(),
                    paymentRequest.origin(),
                    paymentRequest.destination(),
                    negativePayment,
                    LocalDateTime.now()
            );

            CardPaymentDto negativeDto = new CardPaymentDto(
                    paymentRequest.authorization(),
                    paymentRequest.origin(),
                    paymentRequest.destination(),
                    negativePayment,
                    LocalDateTime.now()
            );

            Mockito.when(cardTransactionUseCase.processCardPayment(any()))
                    .thenReturn(negativeDto);

            String requestJson = objectMapper.writeValueAsString(negativeRequest);

            mockMvc.perform(post("/api/speakly-bank/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("SUCCESS"))
                    .andExpect(jsonPath("$.payment.amount").value(-100.00));
        }
    }

    @Nested
    class ErrorTests {

        @Test
        void shouldReturnNotFoundWhenCardDoesNotExist() throws Exception {
            Mockito.when(cardTransactionUseCase.processCardPayment(any()))
                    .thenThrow(new ResourceNotFoundException("Credit card not found: 4532015112830366"));

            String requestJson = objectMapper.writeValueAsString(paymentRequest);

            mockMvc.perform(post("/api/speakly-bank/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnErrorWhenInsufficientBalance() throws Exception {
            Mockito.when(cardTransactionUseCase.processCardPayment(any()))
                    .thenThrow(new BusinessException("Insufficient balance in source account"));

            String requestJson = objectMapper.writeValueAsString(paymentRequest);

            mockMvc.perform(post("/api/speakly-bank/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void shouldReturnErrorWhenCreditCardExpired() throws Exception {
            Mockito.when(cardTransactionUseCase.processCardPayment(any()))
                    .thenThrow(new BusinessException("Credit card is expired"));

            String requestJson = objectMapper.writeValueAsString(paymentRequest);

            mockMvc.perform(post("/api/speakly-bank/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void shouldReturnErrorWhenSelfTransfer() throws Exception {
            DestinationDto sameDestination = new DestinationDto("ES9121000418450200051332");
            CardPaymentRequest selfTransferRequest = new CardPaymentRequest(
                    paymentRequest.authorization(),
                    paymentRequest.origin(),
                    sameDestination,
                    paymentRequest.payment(),
                    LocalDateTime.now()
            );

            Mockito.when(cardTransactionUseCase.processCardPayment(any()))
                    .thenThrow(new BusinessException("Cannot transfer funds to the same account"));

            String requestJson = objectMapper.writeValueAsString(selfTransferRequest);

            mockMvc.perform(post("/api/speakly-bank/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }
    }
}
