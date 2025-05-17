package com.mazyfood.order.application.service.order;

import com.mazyfood.order.application.port.in.order.AddProductToOrderUseCase;
import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;

import java.math.BigDecimal;

public class AddProductToOrderService implements AddProductToOrderUseCase {

    private final OrderRepository orderRepository;

    public AddProductToOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addProductToOrder(OrderId orderId, int productId, String productName, BigDecimal price, int quantity) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.addProduct(productId, productName, price, quantity);
        orderRepository.save(order);
        return order;
    }
}
