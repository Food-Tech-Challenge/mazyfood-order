package com.mazyfood.order.application.service.order.payment;

import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReceiveOrderPaymentServiceTest {

    private OrderRepository orderRepository;
    private ReceiveOrderPaymentService receiveOrderPaymentService;

    private OrderId orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        receiveOrderPaymentService = new ReceiveOrderPaymentService(orderRepository);
        orderId = new OrderId(1);
        order = mock(Order.class);
    }

    @Test
    void testReceivePaymentWhenAuthorizedAndOrderIsInitiated() throws OrderPaymentException, OrderNotFoundException {
        when(order.getStatus()).thenReturn(OrderStatus.INICIADO);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        receiveOrderPaymentService.receivePayment(orderId, true);

        verify(order).setStatus(OrderStatus.RECEBIDO);
        verify(orderRepository).save(order);
    }

    @Test
    void testReceivePaymentWhenNotAuthorizedDoesNotChangeStatus() throws OrderPaymentException, OrderNotFoundException {
        when(order.getStatus()).thenReturn(OrderStatus.INICIADO);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        receiveOrderPaymentService.receivePayment(orderId, false);

        verify(order, never()).setStatus(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testReceivePaymentThrowsExceptionWhenOrderIsNotInitiated() {
        when(order.getStatus()).thenReturn(OrderStatus.RECEBIDO);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderPaymentException.class, () -> {
            receiveOrderPaymentService.receivePayment(orderId, true);
        });

        verify(order, never()).setStatus(any());
        verify(orderRepository, never()).save(any());
    }
}
