package com.mazyfood.order.application.port.in.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderNotFoundExceptionTest {

    @Test
    void testConstructorCreatesInstance() {
        OrderNotFoundException exception = new OrderNotFoundException();
        assertNotNull(exception);
    }
}