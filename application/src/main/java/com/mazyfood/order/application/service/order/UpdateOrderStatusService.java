package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.UpdateOrderStatusUseCase;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;

import java.util.Optional;

public class UpdateOrderStatusService implements UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;

    public UpdateOrderStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> updateStatus(OrderId orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setStatus(OrderStatus.valueOf(status));
            orderRepository.save(order.get());
        }

        return order;
    }
}
