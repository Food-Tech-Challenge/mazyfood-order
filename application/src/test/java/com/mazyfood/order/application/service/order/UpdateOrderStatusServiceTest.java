package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateOrderStatusServiceTest {

    private OrderRepository orderRepository;
    private UpdateOrderStatusService updateOrderStatusService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        updateOrderStatusService = new UpdateOrderStatusService(orderRepository);
    }

    @Test
    void testUpdateStatusWhenOrderExists() {
        OrderId orderId = new OrderId(1);
        Order order = mock(Order.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = updateOrderStatusService.updateStatus(orderId, "RECEBIDO");

        verify(order).setStatus(OrderStatus.RECEBIDO);
        verify(orderRepository).save(order);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    void testUpdateStatusWhenOrderDoesNotExist() {
        OrderId orderId = new OrderId(99);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<Order> result = updateOrderStatusService.updateStatus(orderId, "RECEBIDO");

        assertFalse(result.isPresent());
        verify(orderRepository, never()).save(any());
    }
}
