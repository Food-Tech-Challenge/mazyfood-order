package com.mazyfood.order.adapter.out;

import com.mazyfood.order.adapter.in.rest.order.payment.ReceivePaymentRequestModel;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MockMercadoPagoPaymentGatewayTest {

    private RestTemplate restTemplate;
    private MockMercadoPagoPaymentGateway gateway;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        gateway = new MockMercadoPagoPaymentGateway(restTemplate);
    }

    @Test
    void testAuthorizePaymentSuccess() {
        OrderId orderId = new OrderId(10);
        String targetUrl = "http://localhost/mock";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(targetUrl),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ReceivePaymentRequestModel>>any(),
                eq(String.class)
        )).thenReturn(responseEntity);

        boolean result = gateway.authorizePayment(orderId, targetUrl);

        assertTrue(result);
        verify(restTemplate).exchange(
                eq(targetUrl),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ReceivePaymentRequestModel>>any(),
                eq(String.class)
        );
    }

    @Test
    void testAuthorizePaymentFailureDueToException() {
        OrderId orderId = new OrderId(99);
        String targetUrl = "http://localhost/fail";

        when(restTemplate.exchange(
                eq(targetUrl),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ReceivePaymentRequestModel>>any(),
                eq(String.class)
        )).thenThrow(new RuntimeException("Connection failed"));

        boolean result = gateway.authorizePayment(orderId, targetUrl);

        assertFalse(result);
        verify(restTemplate).exchange(
                eq(targetUrl),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ReceivePaymentRequestModel>>any(),
                eq(String.class)
        );
    }
}
