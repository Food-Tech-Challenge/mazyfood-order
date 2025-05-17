package com.mazyfood.order.application.port.in.order;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;

import java.util.Optional;

public interface UpdateOrderStatusUseCase {
    Optional<Order> updateStatus(OrderId orderId, String status);
}
