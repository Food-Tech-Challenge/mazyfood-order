package com.mazyfood.order.adapter.in.sqs.payment;

public record ReceivePaymentRequestModel(int orderId, String paymentStatus) {
}
