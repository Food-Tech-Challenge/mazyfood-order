package com.mazyfood.order.adapter.in.sqs.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.application.port.in.order.payment.ReceiveOrderPaymentUseCase;
import com.mazyfood.order.application.service.order.payment.OrderPaymentException;
import com.mazyfood.order.model.order.OrderId;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static ch.qos.logback.core.util.StringUtil.isNullOrEmpty;

@Service
public class ReceivePaymentListener {

    private final ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase;

    public ReceivePaymentListener(ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase) {
        this.receiveOrderPaymentUseCase = receiveOrderPaymentUseCase;
    }

    @SqsListener("${events.queues.payment-status}")
    public void processPayment(String message) throws JsonProcessingException, OrderPaymentException, OrderNotFoundException {
        if (isNullOrEmpty(message)) {
            throw new IllegalArgumentException("Message is empty!");
        }

        ReceivePaymentRequestModel receivePaymentRequest = new ObjectMapper().readValue(message, ReceivePaymentRequestModel.class);
        OrderId orderId = new OrderId(receivePaymentRequest.orderId());
        receiveOrderPaymentUseCase.receivePayment(
            orderId, Objects.equals(receivePaymentRequest.paymentStatus(), "PAID")
        );
    }
}
