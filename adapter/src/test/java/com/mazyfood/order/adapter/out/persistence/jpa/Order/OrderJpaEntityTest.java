package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderJpaEntityTest {

    @Test
    void testSettersAndGetters() {
        OrderJpaEntity entity = new OrderJpaEntity();

        entity.setId(10);
        entity.setCustomerId(123);
        entity.setStatus(OrderStatus.EM_PREPARO);

        OrderProductJpaEntity product1 = new OrderProductJpaEntity();
        product1.setProductId(1);
        product1.setProductName("Refri");
        product1.setQuantity(2);
        product1.setPrice(null);
        product1.setOrder(entity);

        OrderProductJpaEntity product2 = new OrderProductJpaEntity();
        product2.setProductId(2);
        product2.setProductName("Pizza");
        product2.setQuantity(1);
        product2.setPrice(null);
        product2.setOrder(entity);

        entity.setProducts(List.of(product1, product2));

        assertEquals(10, entity.getId());
        assertEquals(123, entity.getCustomerId());
        assertEquals(OrderStatus.EM_PREPARO, entity.getStatus());

        List<OrderProductJpaEntity> products = entity.getProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Refri", products.get(0).getProductName());
        assertEquals("Pizza", products.get(1).getProductName());
        assertEquals(entity, products.get(0).getOrder());
    }
}
