package com.mazyfood.order.application.service.order.payment;

import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.out.PaymentGateway;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PayOrderServiceTest {

    private OrderRepository orderRepository;
    private PaymentGateway paymentGateway;
    private PayOrderService payOrderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        paymentGateway = mock(PaymentGateway.class);
        payOrderService = new PayOrderService(orderRepository, paymentGateway);
    }

    @Test
    void testProcessPaymentSuccess() throws Exception {
        OrderId orderId = new OrderId(1);
        Order order = new Order(123);
        order.setId(orderId);
        order.addProduct(1, "Refri", new BigDecimal("50.00"), 1); // total = 50.00

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentGateway.requestPayment(orderId, new BigDecimal("50.00"), "PIX")).thenReturn(true);

        String result = payOrderService.processPayment(orderId, "PIX");

        assertEquals("Order payment requested successfully", result);
        verify(paymentGateway).requestPayment(orderId, new BigDecimal("50.00"), "PIX");
    }

    @Test
    void testProcessPaymentFails() throws Exception {
        OrderId orderId = new OrderId(2);
        Order order = new Order(456);
        order.setId(orderId);
        order.setStatus(OrderStatus.INICIADO);
        order.addProduct(99, "Burger", new BigDecimal("30.00"), 1); // total: 30.00

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentGateway.requestPayment(orderId, new BigDecimal("30.00"), "CREDIT_CARD")).thenReturn(false);

        String result = payOrderService.processPayment(orderId, "CREDIT_CARD");

        assertEquals("Order payment request failed", result);
        verify(paymentGateway).requestPayment(orderId, new BigDecimal("30.00"), "CREDIT_CARD");
    }

    @Test
    void testProcessPaymentThrowsOrderNotFoundException() {
        OrderId orderId = new OrderId(99);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            payOrderService.processPayment(orderId, "PIX");
        });

        verify(paymentGateway, never()).requestPayment(any(), any(), any());
    }

    @Test
    void testProcessPaymentThrowsOrderPaymentExceptionForInvalidStatus() {
        OrderId orderId = new OrderId(3);
        Order order = new Order(789);
        order.setId(orderId);
        order.setStatus(OrderStatus.PRONTO);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderPaymentException exception = assertThrows(OrderPaymentException.class, () -> {
            payOrderService.processPayment(orderId, "PIX");
        });

        assertEquals("Order cannot be paid", exception.getMessage());
        verify(paymentGateway, never()).requestPayment(any(), any(), any());
    }
}
