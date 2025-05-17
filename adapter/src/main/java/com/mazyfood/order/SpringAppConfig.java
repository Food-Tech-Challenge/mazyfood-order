package com.mazyfood.order;

import com.mazyfood.order.application.port.in.order.*;
import com.mazyfood.order.application.port.in.order.payment.PayOrderUseCase;
import com.mazyfood.order.application.port.in.order.payment.ReceiveOrderPaymentUseCase;
import com.mazyfood.order.application.port.out.PaymentGateway;
import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.application.service.order.*;
import com.mazyfood.order.application.service.order.payment.PayOrderService;
import com.mazyfood.order.application.service.order.payment.ReceiveOrderPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringAppConfig {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentGateway paymentGateway;

    @Bean
    CreateOrderUseCase createOrderUseCase() {
        return new CreateOrderService(orderRepository);
    }

    @Bean
    GetOrderUseCase getOrderUseCase() {
        return new GetOrderService(orderRepository);
    }

    @Bean
    AddProductToOrderUseCase addProductToOrderUseCase() {
        return new AddProductToOrderService(orderRepository);
    }

    @Bean
    GetAllOrdersUseCase getAllOrdersUseCase() {
        return new GetAllOrdersService(orderRepository);
    }

    @Bean
    GetOrderedOrdersUseCase getOrderedOrdersUseCase() {
        return new GetOrderedOrdersService(orderRepository);
    }

    @Bean
    PayOrderUseCase payOrderUseCase() {
        return new PayOrderService(orderRepository, paymentGateway);
    }

    @Bean
    ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase() {
        return new ReceiveOrderPaymentService(orderRepository);
    }

    @Bean
    UpdateOrderStatusUseCase updateOrderStatusUseCase() {
        return new UpdateOrderStatusService(orderRepository);
    }
}
