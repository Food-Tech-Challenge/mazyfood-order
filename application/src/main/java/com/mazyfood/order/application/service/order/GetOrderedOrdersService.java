package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.GetOrderedOrdersUseCase;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;

import java.util.List;

public class GetOrderedOrdersService implements GetOrderedOrdersUseCase {
    private final OrderRepository orderRepository;

    public GetOrderedOrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrderedOrders() {
        return orderRepository.getOrdered();
    }
}
