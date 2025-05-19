package com.mazyfood.order.application.service.order.payment;

import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.in.order.payment.ReceiveOrderPaymentUseCase;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;

public class ReceiveOrderPaymentService implements ReceiveOrderPaymentUseCase {
    private final OrderRepository orderRepository;

    public ReceiveOrderPaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void receivePayment(OrderId orderId, boolean authorized) throws OrderPaymentException, OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getStatus() != OrderStatus.INICIADO) {
            throw new OrderPaymentException("Order already paid.");
        }
        if (authorized) {
            order.setStatus(OrderStatus.RECEBIDO);
            orderRepository.save(order);
        }
    }
}
