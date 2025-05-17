package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderJpaEntityTest {

    @Test
    void testAllFieldsAndRelationships() {
        OrderJpaEntity orderEntity = new OrderJpaEntity();

        // Set basic fields
        orderEntity.setId(100);
        orderEntity.setCustomerId(123);
        orderEntity.setStatus(OrderStatus.PRONTO);

        assertEquals(100, orderEntity.getId());
        assertEquals(123, orderEntity.getCustomerId());
        assertEquals(OrderStatus.PRONTO, orderEntity.getStatus());

        // Setup related product entities
        OrderProductJpaEntity product1 = new OrderProductJpaEntity();
        product1.setProductId(1);
        product1.setProductName("Pizza");
        product1.setQuantity(2);
        product1.setPrice(new BigDecimal("30.00"));
        product1.setOrder(orderEntity);

        OrderProductJpaEntity product2 = new OrderProductJpaEntity();
        product2.setProductId(2);
        product2.setProductName("Refri");
        product2.setQuantity(1);
        product2.setPrice(new BigDecimal("7.00"));
        product2.setOrder(orderEntity);

        // Link products to order
        orderEntity.setProducts(List.of(product1, product2));

        List<OrderProductJpaEntity> products = orderEntity.getProducts();
        assertNotNull(products);
        assertEquals(2, products.size());

        // Check reverse relationship
        assertEquals(orderEntity, products.get(0).getOrder());
        assertEquals("Pizza", products.get(0).getProductName());
        assertEquals("Refri", products.get(1).getProductName());
    }


    @Test
    void testHasProductsTrue() {
        OrderJpaEntity order = new OrderJpaEntity();
        order.setId(1);
        order.setCustomerId(123);
        order.setStatus(OrderStatus.RECEBIDO);

        OrderProductJpaEntity product = new OrderProductJpaEntity();
        product.setProductId(1);
        product.setProductName("Pizza");
        product.setQuantity(2);
        product.setOrder(order);

        order.setProducts(List.of(product));

        assertTrue(order.hasProducts());
    }

    @Test
    void testHasProductsFalseWhenNull() {
        OrderJpaEntity order = new OrderJpaEntity();
        order.setProducts(null);

        assertFalse(order.hasProducts());
    }

    @Test
    void testHasProductsFalseWhenEmpty() {
        OrderJpaEntity order = new OrderJpaEntity();
        order.setProducts(List.of());

        assertFalse(order.hasProducts());
    }
}
