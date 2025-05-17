package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetOrderServiceTest {

    private OrderRepository orderRepository;
    private GetOrderService getOrderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        getOrderService = new GetOrderService(orderRepository);
    }

    @Test
    void testGetOrderWhenOrderExists() throws OrderNotFoundException {
        OrderId orderId = new OrderId(1);
        Order order = mock(Order.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = getOrderService.getOrder(orderId);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testGetOrderWhenOrderDoesNotExistThrowsException() {
        OrderId orderId = new OrderId(99);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            getOrderService.getOrder(orderId);
        });

        verify(orderRepository).findById(orderId);
    }
}
