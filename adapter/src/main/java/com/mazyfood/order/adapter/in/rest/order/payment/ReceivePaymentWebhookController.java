package com.mazyfood.order.adapter.in.rest.order.payment;

import com.mazyfood.order.application.port.in.order.payment.ReceiveOrderPaymentUseCase;
import com.mazyfood.order.application.service.order.payment.OrderPaymentException;
import com.mazyfood.order.model.order.OrderId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class ReceivePaymentWebhookController {
    private final ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase;

    public ReceivePaymentWebhookController(ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase) {
        this.receiveOrderPaymentUseCase = receiveOrderPaymentUseCase;
    }

    @PostMapping("/payment")
    public ResponseEntity<ReceivePaymentResponseModel> processPayment(@RequestBody ReceivePaymentRequestModel receivePaymentRequestModel) {
        OrderId orderId = new OrderId(receivePaymentRequestModel.orderId());
        try {
            receiveOrderPaymentUseCase.receivePayment(orderId, receivePaymentRequestModel.authorized());
        } catch (OrderPaymentException e) {
            ReceivePaymentResponseModel receivePaymentResponseModel = new ReceivePaymentResponseModel(orderId.value(), e.getMessage());
            return ResponseEntity.badRequest().body(receivePaymentResponseModel);
        }
        return ResponseEntity.accepted().build();
    }
}
