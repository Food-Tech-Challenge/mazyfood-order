package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderProduct;

import java.util.List;

final class OrderMapper {
    private OrderMapper() {
    }

    static OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity orderJpaEntity = new OrderJpaEntity();
        if (order.getId() != null) {
            orderJpaEntity.setId(order.getOrderId());
        }
        orderJpaEntity.setCustomerId(orderJpaEntity.getCustomerId());
        orderJpaEntity.setProducts(
                order.orderProducts().stream().map(orderProduct -> toJpaEntity(orderJpaEntity, orderProduct)).toList()
        );
        orderJpaEntity.setStatus(order.getStatus());
        return orderJpaEntity;
    }

    static OrderProductJpaEntity toJpaEntity(OrderJpaEntity orderJpaEntity, OrderProduct orderProduct) {
        OrderProductJpaEntity orderProductJpaEntity = new OrderProductJpaEntity();
        orderProductJpaEntity.setOrder(orderJpaEntity);
        orderProductJpaEntity.setProductId(orderProduct.getProductId());
        orderProductJpaEntity.setProductName(orderProduct.getProductName());
        orderProductJpaEntity.setQuantity(orderProduct.getQuantity());
        return orderProductJpaEntity;
    }

    static Order toModelEntity(OrderJpaEntity jpaEntity) {
        Order order = new Order();
        return updateModelEntity(order, jpaEntity);
    }

    static Order updateModelEntity(Order order, OrderJpaEntity jpaEntity) {
        order.setId(new OrderId(jpaEntity.getId()));
        order.setStatus(jpaEntity.getStatus());
        for (OrderProductJpaEntity orderProductJpaEntity : jpaEntity.getProducts()) {
            order.addProduct(
                orderProductJpaEntity.getProductId(),
                orderProductJpaEntity.getProductName(),
                orderProductJpaEntity.getPrice(),
                orderProductJpaEntity.getQuantity()
            );
        }
        return order;

    }

    static List<Order> toModelEntities(List<OrderJpaEntity> jpaEntities) {
        return jpaEntities.stream().map(OrderMapper::toModelEntity).toList();
    }

}

