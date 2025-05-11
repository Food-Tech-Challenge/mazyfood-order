package com.mazyfood.order.adapter.in.rest.order;

import com.mazyfood.order.adapter.in.rest.order.dto.OrderProductRequestModel;
import com.mazyfood.order.adapter.in.rest.order.dto.OrderResponse;
import com.mazyfood.order.application.port.in.order.AddProductToOrderUseCase;
import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class AddProductToOrderController {
    private final AddProductToOrderUseCase addProductToOrderUseCase;

    public AddProductToOrderController(AddProductToOrderUseCase addProductToOrderUseCase) {
        this.addProductToOrderUseCase = addProductToOrderUseCase;
    }

    @PutMapping("/{id}/products")
    public ResponseEntity<OrderResponse> addProductToOrder(@PathVariable int id, @RequestBody OrderProductRequestModel orderProductRequestModel) {
        OrderId orderId = new OrderId(id);
        Order order;
        try {
            order = this.addProductToOrderUseCase.addProductToOrder(
                    orderId,
                    orderProductRequestModel.productId(),
                    orderProductRequestModel.productName(),
                    orderProductRequestModel.price(),
                    orderProductRequestModel.quantity()
            );
        } catch (OrderNotFoundException orderNotFoundException) {
            return ResponseEntity.notFound().build();
        }
        OrderResponse orderResponse = OrderResponse.fromDomain(order);
        return ResponseEntity.ok(orderResponse);
    }
}
