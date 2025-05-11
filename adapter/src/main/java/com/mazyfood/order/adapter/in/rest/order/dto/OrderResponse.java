package com.mazyfood.order.adapter.in.rest.order.dto;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public record OrderResponse(int id, OrderStatus orderStatus, int customerId,
                            List<OrderProductInListWebModel> products) {
    public static OrderResponse fromDomain(Order order) {
        List<OrderProductInListWebModel> products = order.orderProducts().stream().map(
                OrderProductInListWebModel::fromDomainModel
        ).toList();
        return new OrderResponse(order.getOrderId(), order.getStatus(), order.getCustomerId(), products);
    }
}
