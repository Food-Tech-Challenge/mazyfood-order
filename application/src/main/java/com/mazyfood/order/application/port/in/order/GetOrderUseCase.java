package com.mazyfood.order.application.port.in.order;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;

import java.util.Optional;

public interface GetOrderUseCase {
    Optional<Order> getOrder(OrderId orderId);
}
