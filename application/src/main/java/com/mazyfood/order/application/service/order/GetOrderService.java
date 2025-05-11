package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.GetOrderUseCase;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;

import java.util.Optional;

public class GetOrderService implements GetOrderUseCase {
    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> getOrder(OrderId orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order;
    }
}
