package com.mazyfood.order.application.port.in.order.payment;

import com.mazyfood.order.application.service.order.payment.OrderPaymentException;
import com.mazyfood.order.model.order.OrderId;

public interface PayOrderUseCase {
    String processPayment(OrderId orderId, String paymentMethod) throws OrderPaymentException;
}
