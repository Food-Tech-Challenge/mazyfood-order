package com.mazyfood.order.application.service.order.payment;

import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.in.order.payment.PayOrderUseCase;
import com.mazyfood.order.application.port.out.PaymentGateway;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;

public class PayOrderService implements PayOrderUseCase {
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public PayOrderService(OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public String processPayment(OrderId orderId, String paymentMethod) throws OrderPaymentException, OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getStatus() != OrderStatus.INICIADO) {
            throw new OrderPaymentException("Order cannot be paid");
        }
        boolean paymentRequested = paymentGateway.requestPayment(order.getId(), order.getTotal(), paymentMethod);
        if (paymentRequested) {
            return "Order payment requested successfully";
        } else {
            return "Order payment request failed";
        }

    }
}
