package com.mazyfood.order.adapter.in.sqs.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.application.port.in.order.payment.ReceiveOrderPaymentUseCase;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceivePaymentListenerTest {

    private ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase;
    private ReceivePaymentListener listener;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        receiveOrderPaymentUseCase = mock(ReceiveOrderPaymentUseCase.class);
        listener = new ReceivePaymentListener(receiveOrderPaymentUseCase);
    }

    @Test
    void shouldThrowExceptionWhenMessageIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> listener.processPayment("")
        );
        assertEquals("Message is empty!", exception.getMessage());
    }

    @Test
    void shouldProcessPaidPaymentSuccessfully() throws Exception {
        Integer orderId = 12345;
        String jsonMessage = String.format("{\"orderId\":\"%d\",\"paymentStatus\":\"PAID\"}", orderId);

        listener.processPayment(jsonMessage);

        ArgumentCaptor<OrderId> orderIdCaptor = ArgumentCaptor.forClass(OrderId.class);
        ArgumentCaptor<Boolean> statusCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(receiveOrderPaymentUseCase).receivePayment(orderIdCaptor.capture(), statusCaptor.capture());

        assertEquals(orderId, orderIdCaptor.getValue().value());
        assertTrue(statusCaptor.getValue());
    }

    @Test
    void shouldProcessNotPaidPaymentSuccessfully() throws Exception {
        Integer orderId = 67890;
        String jsonMessage = String.format("{\"orderId\":\"%d\",\"paymentStatus\":\"PENDING\"}", orderId);

        listener.processPayment(jsonMessage);

        ArgumentCaptor<OrderId> orderIdCaptor = ArgumentCaptor.forClass(OrderId.class);
        ArgumentCaptor<Boolean> statusCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(receiveOrderPaymentUseCase).receivePayment(orderIdCaptor.capture(), statusCaptor.capture());

        assertEquals(orderId, orderIdCaptor.getValue().value());
        assertFalse(statusCaptor.getValue());
    }

    @Test
    void shouldThrowJsonProcessingExceptionForInvalidJson() {
        String invalidJson = "{ invalid json }";

        assertThrows(JsonProcessingException.class, () -> listener.processPayment(invalidJson));
    }
}
