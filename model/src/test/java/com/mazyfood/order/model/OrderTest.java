package com.mazyfood.order.model;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderProduct;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


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
}
