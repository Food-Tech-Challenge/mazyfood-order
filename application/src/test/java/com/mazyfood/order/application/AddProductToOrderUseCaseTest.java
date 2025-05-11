package com.mazyfood.order.application;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderProduct;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AddProductToOrderUseCaseTest {
    @Test
    void testAddProductToOrder() {
        Integer customerId = null;

        int productId = 1;
        String productName = "Hanburguer";
        BigDecimal price = new BigDecimal("23.99");
        int quantity = 2;

        Order order = new Order(customerId);
        order.addProduct(productId, productName, price, quantity);
        OrderProduct orderProduct = order.getProducts().get(productId);
        assertThat(orderProduct).isNotNull();
        assertThat(orderProduct.getQuantity()).isEqualTo(2);
    }
}
