package com.mazyfood.order.application.service.order;


import com.mazyfood.order.application.port.in.order.CreateOrderUseCase;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;

import java.util.Objects;

public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public CreateOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Integer customerId) {
        Order order;
        if (Objects.isNull(customerId)) {
            order = new Order();
        } else {
            order = new Order(customerId);
        }
        orderRepository.save(order);
        return order;
    }
}