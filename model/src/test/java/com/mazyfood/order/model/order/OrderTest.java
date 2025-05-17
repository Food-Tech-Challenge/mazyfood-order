package com.mazyfood.order.model.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class OrderTest {

    @Test
    void testAddProductToOrderWithCustomerIdentified() {
        Order order = new Order(123);
        int productId = 1;
        BigDecimal price = new BigDecimal("10");
        String productName = "Hamburguer";
        int quantity = 1;

        order.addProduct(productId, productName, price, quantity);

        OrderProduct expected = new OrderProduct(productId, productName, price, quantity);
        OrderProduct actual = order.getProducts().get(productId);

        assertNotNull(actual);
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getQuantity(), actual.getQuantity());

        assertEquals(1, order.getProducts().size());
        assertTrue(order.getProducts().containsKey(productId));
    }

    @Test
    void testAddProductToOrderWithoutIdentifiedCustomer() {
        Order order = new Order();
        int productId = 1;
        BigDecimal price = new BigDecimal("10");
        String productName = "Hamburguer";
        int quantity = 1;

        order.addProduct(productId, productName, price, quantity);

        OrderProduct expected = new OrderProduct(productId, productName, price, quantity);
        OrderProduct actual = order.getProducts().get(productId);

        assertNotNull(actual);
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getQuantity(), actual.getQuantity());

        assertEquals(1, order.getProducts().size());
        assertTrue(order.getProducts().containsKey(productId));
    }

    @Test
    void testGetOrderId() {
        Order order = new Order(123);
        OrderId mockOrderId = mock(OrderId.class);
        when(mockOrderId.value()).thenReturn(456);
        order.setId(mockOrderId);

        int orderId = order.getOrderId();

        assertEquals(456, orderId);
        verify(mockOrderId).value();
    }

    @Test
    void testOrderProductsReturnsAddedProducts() {
        Order order = new Order(123);
        order.addProduct(1, "Batata M", new BigDecimal("10.00"), 2);
        order.addProduct(2, "Batata G", new BigDecimal("20.00"), 1);

        List<OrderProduct> products = order.orderProducts();

        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getProductId() == 1 && p.getProductName().equals("Batata M")));
        assertTrue(products.stream().anyMatch(p -> p.getProductId() == 2 && p.getProductName().equals("Batata G")));
    }

    @Test
    void testOrderProductsIsUnmodifiable() {
        Order order = new Order(123);
        order.addProduct(1, "Refrigerante Refil", new BigDecimal("10.00"), 1);

        List<OrderProduct> products = order.orderProducts();

        assertThrows(UnsupportedOperationException.class, () -> {
            products.add(mock(OrderProduct.class));
        });
    }
}
