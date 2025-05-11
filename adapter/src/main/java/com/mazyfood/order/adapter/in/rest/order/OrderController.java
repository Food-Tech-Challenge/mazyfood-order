package com.mazyfood.order.adapter.in.rest.order;

import com.mazyfood.order.adapter.in.rest.order.dto.CreateOrderRequest;
import com.mazyfood.order.adapter.in.rest.order.dto.OrderInListWebModel;
import com.mazyfood.order.adapter.in.rest.order.dto.OrderResponse;
import com.mazyfood.order.application.port.in.order.CreateOrderUseCase;
import com.mazyfood.order.application.port.in.order.GetAllOrdersUseCase;
import com.mazyfood.order.application.port.in.order.GetOrderUseCase;
import com.mazyfood.order.application.port.in.order.GetOrderedOrdersUseCase;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final GetOrderedOrdersUseCase getOrderedOrdersUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase, GetAllOrdersUseCase getAllOrdersUseCase, GetOrderedOrdersUseCase getOrderedOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.getOrderedOrdersUseCase = getOrderedOrdersUseCase;
    }

    @PostMapping
    ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        Integer customerId = createOrderRequest.customerId().orElse(null);

        Order order = createOrderUseCase.createOrder(customerId);
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable int id) {
        OrderId orderId = new OrderId(id);
        Order order = getOrderUseCase.getOrder(orderId).get();
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }

    @GetMapping
    public List<OrderInListWebModel> getAllOrders() {
        List<Order> orders = getAllOrdersUseCase.getAllOrders();
        return orders.stream().map(OrderInListWebModel::fromDomainModel).toList();
    }

    @GetMapping("/ordered")
    public List<OrderInListWebModel> getOrderedProducts() {
        List<Order> orders = getOrderedOrdersUseCase.getOrderedOrders();
        return orders.stream().map(OrderInListWebModel::fromDomainModel).toList();
    }
}