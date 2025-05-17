package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllOrdersServiceTest {

    private OrderRepository orderRepository;
    private GetAllOrdersService getAllOrdersService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        getAllOrdersService = new GetAllOrdersService(orderRepository);
    }

    @Test
    void testGetAllOrdersReturnsListOfOrders() {
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> orders = getAllOrdersService.getAllOrders();

        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        verify(orderRepository).findAll();
    }
}
