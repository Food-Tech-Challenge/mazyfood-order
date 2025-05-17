package com.mazyfood.order.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderProduct {
    private final int productId;
    private final String productName;
    private final BigDecimal price;
    private int quantity;

    public void increaseQuantityBy(int value) {
        this.quantity = quantity + value;
    }

    public BigDecimal getTotal() {
        return this.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
