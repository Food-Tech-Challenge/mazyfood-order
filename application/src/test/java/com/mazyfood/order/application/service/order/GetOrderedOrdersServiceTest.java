package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetOrderedOrdersServiceTest {

    private OrderRepository orderRepository;
    private GetOrderedOrdersService getOrderedOrdersService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        getOrderedOrdersService = new GetOrderedOrdersService(orderRepository);
    }

    @Test
    void testGetOrderedOrdersReturnsListOfOrderedOrders() {
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        when(orderRepository.getOrdered()).thenReturn(List.of(order1, order2));

        List<Order> result = getOrderedOrdersService.getOrderedOrders();

        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
        verify(orderRepository).getOrdered();
    }
}
