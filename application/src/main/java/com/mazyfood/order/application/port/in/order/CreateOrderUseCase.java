package com.mazyfood.order.application.port.in.order;

import com.mazyfood.order.model.order.Order;

public interface CreateOrderUseCase {
    Order createOrder(Integer customerId);
}
