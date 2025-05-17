package com.mazyfood.order.application.port.in.order;

import com.mazyfood.order.model.order.Order;

import java.util.List;

public interface GetOrderedOrdersUseCase {
    List<Order> getOrderedOrders();
}
