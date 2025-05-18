package com.mazyfood.order.application.port.out;

import com.mazyfood.order.model.order.OrderId;

import java.math.BigDecimal;

public interface PaymentGateway {
    boolean requestPayment(OrderId orderId, BigDecimal total, String paymentMethod);
}
