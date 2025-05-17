package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.GetAllOrdersUseCase;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;

import java.util.List;

public class GetAllOrdersService implements GetAllOrdersUseCase {
    private final OrderRepository orderRepository;

    public GetAllOrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
