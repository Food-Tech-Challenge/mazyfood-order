package com.mazyfood.order.adapter.out.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mazyfood.order.adapter.in.sqs.payment.ReceivePaymentRequestModel;
import com.mazyfood.order.model.order.OrderId;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MazyFoodPaymentGatewayTest {

    private SqsTemplate sqsTemplate;
    private MazyFoodPaymentGateway gateway;

    private final String queue = "mock-queue-url";

    @BeforeEach
    void setUp() {
        sqsTemplate = mock(SqsTemplate.class);
        gateway = new MazyFoodPaymentGateway(sqsTemplate, queue);
    }

    @Test
    void testRequestPaymentSuccess() {
        OrderId orderId = new OrderId(1);
        BigDecimal price = new BigDecimal("25.00");

        boolean result = gateway.requestPayment(orderId, price, "PIX");

        assertTrue(result);
        verify(sqsTemplate).send(eq(queue), contains("\"orderId\":1"));
    }

    @Test
    void testRequestPaymentFailsDueToSerialization() throws Exception {
        MazyFoodPaymentGateway faultyGateway = new MazyFoodPaymentGateway(sqsTemplate, queue) {
            @Override
            String objectToString(Object object) throws JsonProcessingException {
                throw new JsonProcessingException("forced failure") {};
            }
        };

        OrderId orderId = new OrderId(99);
        boolean result = faultyGateway.requestPayment(orderId, BigDecimal.TEN, "PIX");

        assertFalse(result);
        verify(sqsTemplate, never()).send(any(), any());
    }

    @Test
    void testObjectToStringReturnsJsonString() throws Exception {
        ReceivePaymentRequestModel model = new ReceivePaymentRequestModel(123, "PAID");

        String json = gateway.objectToString(model);

        assertTrue(json.contains("\"orderId\":123"));
        assertTrue(json.contains("\"paymentStatus\":\"PAID\""));
    }

}