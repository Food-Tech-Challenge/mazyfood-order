package com.mazyfood.order.application;

import com.mazyfood.order.model.order.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateOrderUseCaseTest {
    @Test
    public void testCreateOrder() {
        Order order = new Order(123);
        assertThat(order.getCustomerId()).isEqualTo(123);
    }
}
