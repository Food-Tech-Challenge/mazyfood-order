package com.mazyfood.order.adapter.out.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mazyfood.order.adapter.in.sqs.payment.ReceivePaymentRequestModel;
import com.mazyfood.order.application.port.out.PaymentGateway;
import com.mazyfood.order.model.order.OrderId;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(name = "paymentGateway", matchIfMissing = true)
public class MazyFoodPaymentGateway implements PaymentGateway {

    private final SqsTemplate sqsTemplate;
    private final String orderToPayQueue;

    public MazyFoodPaymentGateway(
            SqsTemplate sqsTemplate,
            @Value("${events.queues.order-to-pay}") String orderToPayQueue) {
        this.sqsTemplate = sqsTemplate;
        this.orderToPayQueue = orderToPayQueue;
    }

    @Override
    public boolean requestPayment(OrderId orderId, BigDecimal price, String paymentMethod) {
        ReceivePaymentRequestModel receivePaymentRequestModel = new ReceivePaymentRequestModel(orderId.value(), "PAID");

        try {
            this.sqsTemplate.send(this.orderToPayQueue, this.objectToString(receivePaymentRequestModel));
            return true;
        } catch (JsonProcessingException e) {
            System.out.printf("Error converting object to string: %s%n", e.getMessage());
        }
        return false;
    }

    private String objectToString(Object object) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }
}