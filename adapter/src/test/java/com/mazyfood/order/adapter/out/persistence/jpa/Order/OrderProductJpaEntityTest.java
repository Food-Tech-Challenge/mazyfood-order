package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class OrderProductJpaEntityTest {

    @Test
    void testIsQuantityPositiveTrue() {
        OrderProductJpaEntity product = new OrderProductJpaEntity();
        product.setProductId(1);
        product.setProductName("Refri");
        product.setQuantity(2);
        product.setPrice(new BigDecimal("5.00"));

        assertTrue(product.isQuantityPositive());
    }

    @Test
    void testIsQuantityPositiveFalse() {
        OrderProductJpaEntity product = new OrderProductJpaEntity();
        product.setQuantity(0);

        assertFalse(product.isQuantityPositive());
    }

    @Test
    void testSettersAndGetters() {
        OrderProductJpaEntity product = new OrderProductJpaEntity();
        OrderJpaEntity order = new OrderJpaEntity();

        product.setId(10L);
        product.setOrder(order);
        product.setProductId(123);
        product.setProductName("Pizza");
        product.setPrice(new BigDecimal("29.90"));
        product.setQuantity(3);

        assertEquals(10L, product.getId());
        assertEquals(order, product.getOrder());
        assertEquals(123, product.getProductId());
        assertEquals("Pizza", product.getProductName());
        assertEquals(new BigDecimal("29.90"), product.getPrice());
        assertEquals(3, product.getQuantity());
    }
}
