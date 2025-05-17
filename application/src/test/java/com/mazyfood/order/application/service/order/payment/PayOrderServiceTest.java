package com.mazyfood.order.application.service.order.payment;

import com.mazyfood.order.application.port.out.PaymentGateway;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PayOrderServiceTest {

    private OrderRepository orderRepository;
    private PaymentGateway paymentGateway;
    private PayOrderService payOrderService;

    private OrderId orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        paymentGateway = mock(PaymentGateway.class);
        payOrderService = new PayOrderService(orderRepository, paymentGateway);

        orderId = new OrderId(1);
        order = mock(Order.class);
    }

    @Test
    void testProcessPaymentWhenOrderIsInitiated() throws Exception {
        when(order.getStatus()).thenReturn(OrderStatus.INICIADO);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getScheme()).thenReturn("http");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8080);

        RequestAttributes attributes = mock(RequestAttributes.class);
        when(attributes.resolveReference(RequestAttributes.REFERENCE_REQUEST)).thenReturn(request);

        try (MockedStatic<RequestContextHolder> mocked = mockStatic(RequestContextHolder.class)) {
            mocked.when(RequestContextHolder::getRequestAttributes).thenReturn(attributes);

            String result = payOrderService.processPayment(orderId, "pix");

            verify(paymentGateway).authorizePayment(eq(orderId), eq("http://localhost:8080/orders/payment"));
            assertEquals("Processing order payment", result);
        }
    }

    @Test
    void testProcessPaymentThrowsExceptionIfOrderIsNotInitiated() {
        when(order.getStatus()).thenReturn(OrderStatus.RECEBIDO);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderPaymentException.class, () -> {
            payOrderService.processPayment(orderId, "pix");
        });

        verify(paymentGateway, never()).authorizePayment(any(), any());
    }

    @Test
    void testProcessPaymentThrowsExceptionIfNoHttpRequestAvailable() {
        when(order.getStatus()).thenReturn(OrderStatus.INICIADO);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        try (MockedStatic<RequestContextHolder> mocked = mockStatic(RequestContextHolder.class)) {
            mocked.when(RequestContextHolder::getRequestAttributes).thenReturn(null);

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                payOrderService.processPayment(orderId, "pix");
            });

            assertEquals("Request is not available in the current context", exception.getMessage());
        }

        verify(paymentGateway, never()).authorizePayment(any(), any());
    }
}
