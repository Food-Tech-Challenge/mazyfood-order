package com.mazyfood.order.application.port.in.order.payment;

import com.mazyfood.order.application.service.order.payment.OrderPaymentException;
import com.mazyfood.order.model.order.OrderId;

public interface ReceiveOrderPaymentUseCase {
    void receivePayment(OrderId orderId, boolean authorized) throws OrderPaymentException;
}
