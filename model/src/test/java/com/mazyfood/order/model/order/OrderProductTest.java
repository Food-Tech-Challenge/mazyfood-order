package com.mazyfood.order.model.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderProductTest {

    @Test
    void testGetTotalWithValidQuantityAndPrice() {
        OrderProduct product = new OrderProduct(1, "X-Burger", new BigDecimal("15.50"), 3);

        BigDecimal total = product.getTotal();

        assertEquals(new BigDecimal("46.50"), total);
    }

    @Test
    void testGetTotalAfterIncreasingQuantity() {
        OrderProduct product = new OrderProduct(2, "Batata P", new BigDecimal("8.00"), 1);

        product.increaseQuantityBy(2);
        BigDecimal total = product.getTotal();

        assertEquals(new BigDecimal("24.00"), total);
    }

    @Test
    void testOrderIdConstruction() {
        OrderId id = new OrderId(42);
        assertEquals(42, id.value());
    }
}
