package com.mazyfood.order.application.port.in.order;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;

import java.math.BigDecimal;

public interface AddProductToOrderUseCase {

    Order addProductToOrder(OrderId orderId, int productId, String productName, BigDecimal price, int quantity)
            throws OrderNotFoundException;
}
