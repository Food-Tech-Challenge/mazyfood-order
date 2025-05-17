package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateOrderServiceTest {

    private OrderRepository orderRepository;
    private CreateOrderService createOrderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        createOrderService = new CreateOrderService(orderRepository);
    }

    @Test
    void testCreateOrderWithCustomerId() {
        Integer customerId = 123;

        Order order = createOrderService.createOrder(customerId);

        assertNotNull(order);
        assertEquals(customerId, order.getCustomerId());
        verify(orderRepository).save(order);
    }

    @Test
    void testCreateOrderWithoutCustomerId() {
        Order order = createOrderService.createOrder(null);

        assertNotNull(order);
        assertNull(order.getCustomerId());
        verify(orderRepository).save(order);
    }
}
