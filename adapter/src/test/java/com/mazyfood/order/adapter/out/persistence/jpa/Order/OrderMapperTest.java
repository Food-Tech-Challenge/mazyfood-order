package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderProduct;
import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderMapperTest {

    @Test
    void testToJpaEntityWithNullId() {
        Order order = new Order(123);
        order.setStatus(OrderStatus.INICIADO);
        order.addProduct(1, "Pizza", new BigDecimal("25.00"), 2);

        OrderJpaEntity entity = OrderMapper.toJpaEntity(order);

        assertNull(entity.getId());
        assertEquals(OrderStatus.INICIADO, entity.getStatus());

        List<OrderProductJpaEntity> products = entity.getProducts();
        assertEquals(1, products.size());

        OrderProductJpaEntity product = products.get(0);
        assertEquals(1, product.getProductId());
        assertEquals("Pizza", product.getProductName());
        assertEquals(new BigDecimal("25.00"), product.getPrice());
        assertEquals(2, product.getQuantity());
        assertEquals(entity, product.getOrder());
    }

    @Test
    void testToJpaEntityWithExistingId() {
        Order order = new Order(456);
        order.setId(new OrderId(99));
        order.setStatus(OrderStatus.EM_PREPARO);

        OrderJpaEntity entity = OrderMapper.toJpaEntity(order);

        assertEquals(99, entity.getId());
        assertEquals(OrderStatus.EM_PREPARO, entity.getStatus());
    }

    @Test
    void testToJpaEntityFromProduct() {
        OrderJpaEntity parent = new OrderJpaEntity();
        OrderProduct product = new OrderProduct(10, "Refri", new BigDecimal("5.00"));
        product.increaseQuantityBy(1);

        OrderProductJpaEntity result = OrderMapper.toJpaEntity(parent, product);

        assertEquals(parent, result.getOrder());
        assertEquals(10, result.getProductId());
        assertEquals("Refri", result.getProductName());
        assertEquals(1, result.getQuantity());
    }

    @Test
    void testToModelEntityAndUpdateModelEntity() {
        OrderJpaEntity jpa = new OrderJpaEntity();
        jpa.setId(100);
        jpa.setStatus(OrderStatus.RECEBIDO);

        OrderProductJpaEntity p = new OrderProductJpaEntity();
        p.setProductId(5);
        p.setProductName("Hamburguer");
        p.setPrice(new BigDecimal("30.00"));
        p.setQuantity(2);
        p.setOrder(jpa);
        jpa.setProducts(List.of(p));

        Order model = OrderMapper.toModelEntity(jpa);

        assertEquals(100, model.getOrderId());
        assertEquals(OrderStatus.RECEBIDO, model.getStatus());
        assertEquals(1, model.orderProducts().size());
        OrderProduct mapped = model.orderProducts().get(0);
        assertEquals("Hamburguer", mapped.getProductName());
        assertEquals(2, mapped.getQuantity());
    }

    @Test
    void testToModelEntities() {
        OrderJpaEntity jpa1 = new OrderJpaEntity();
        jpa1.setId(1);
        jpa1.setStatus(OrderStatus.PRONTO);
        jpa1.setProducts(List.of());

        OrderJpaEntity jpa2 = new OrderJpaEntity();
        jpa2.setId(2);
        jpa2.setStatus(OrderStatus.RECEBIDO);
        jpa2.setProducts(List.of());

        List<Order> result = OrderMapper.toModelEntities(List.of(jpa1, jpa2));

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getOrderId());
        assertEquals(2, result.get(1).getOrderId());
    }
}
