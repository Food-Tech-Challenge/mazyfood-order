package com.mazyfood.order.adapter.in.rest.order.dto;

import com.mazyfood.order.model.order.OrderProduct;

import java.math.BigDecimal;

public record OrderProductInListWebModel(
        int id, String name, BigDecimal price, int quantity, BigDecimal total) {

    public static OrderProductInListWebModel fromDomainModel(OrderProduct orderProduct) {
        return new OrderProductInListWebModel(
                orderProduct.getProductId(),
                orderProduct.getProductName(),
                orderProduct.getPrice(),
                orderProduct.getQuantity(),
                orderProduct.getTotal()
        );
    }
}
