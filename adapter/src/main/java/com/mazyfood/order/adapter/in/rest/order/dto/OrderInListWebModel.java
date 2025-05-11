package com.mazyfood.order.adapter.in.rest.order.dto;

import com.mazyfood.order.model.order.Order;

public record OrderInListWebModel(
        int id, int customerId, String category) {

    public static OrderInListWebModel fromDomainModel(Order order) {
        return new OrderInListWebModel(
                order.getOrderId(),
                order.getCustomerId(),
                order.getStatus().name()
        );
    }
}
