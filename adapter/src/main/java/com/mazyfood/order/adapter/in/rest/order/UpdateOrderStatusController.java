package com.mazyfood.order.adapter.in.rest.order;

import com.mazyfood.order.adapter.in.rest.order.dto.OrderRequestModel;
import com.mazyfood.order.adapter.in.rest.order.dto.OrderResponse;
import com.mazyfood.order.application.port.in.order.UpdateOrderStatusUseCase;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class UpdateOrderStatusController {
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    public UpdateOrderStatusController(UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable int id, @RequestBody OrderRequestModel orderRequestModel) {
        OrderId orderId = new OrderId(id);
        String status = orderRequestModel.status();
        Optional<Order> order = updateOrderStatusUseCase.updateStatus(orderId, status);
        if (order.isPresent()) {
            OrderResponse orderResponse = OrderResponse.fromDomain(order.get());
            return ResponseEntity.ok(orderResponse);
        }
        return ResponseEntity.notFound().build();

    }
}
