package com.mazyfood.order.model.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class Order {
    private final Map<Integer, OrderProduct> products = new LinkedHashMap<>();
    private OrderId id;
    private Integer customerId;
    private OrderStatus status;

    public Order(Integer customerId) {
        this.customerId = customerId;
        this.status = OrderStatus.INICIADO;
    }

    public Order() {
        this.status = OrderStatus.INICIADO;
    }

    public int getOrderId() {
        return this.getId().value();
    }

    public List<OrderProduct> orderProducts() {
        return List.copyOf(products.values());
    }

    public void addProduct(int productId, String productName, BigDecimal price, int quantity) {
        products
                .computeIfAbsent(productId, ignored -> new OrderProduct(productId, productName, price))
                .increaseQuantityBy(quantity);
    }
}
