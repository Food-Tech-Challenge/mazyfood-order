package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AddProductToOrderServiceTest {

    private OrderRepository orderRepository;
    private AddProductToOrderService addProductToOrderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        addProductToOrderService = new AddProductToOrderService(orderRepository);
    }

    @Test
    void testAddProductToExistingOrder() throws OrderNotFoundException {
        OrderId orderId = new OrderId(1);
        Order existingOrder = mock(Order.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        Order result = addProductToOrderService.addProductToOrder(orderId, 10, "Coca-Cola", new BigDecimal("5.00"), 2);

        verify(existingOrder).addProduct(10, "Coca-Cola", new BigDecimal("5.00"), 2);
        verify(orderRepository).save(existingOrder);
        assertEquals(existingOrder, result);
    }

    @Test
    void testAddProductToNonExistingOrderThrowsException() {
        OrderId orderId = new OrderId(99);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            addProductToOrderService.addProductToOrder(orderId, 1, "Burger", new BigDecimal("15.00"), 1);
        });

        verify(orderRepository, never()).save(any());
    }
}
