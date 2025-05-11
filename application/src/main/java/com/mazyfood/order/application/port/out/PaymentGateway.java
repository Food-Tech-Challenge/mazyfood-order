package com.mazyfood.order.application.port.out;

import com.mazyfood.order.model.order.OrderId;

public interface PaymentGateway {
    boolean authorizePayment(OrderId orderId, String targetUrl);
}
