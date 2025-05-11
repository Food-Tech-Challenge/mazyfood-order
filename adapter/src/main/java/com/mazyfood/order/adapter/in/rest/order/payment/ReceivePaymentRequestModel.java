package com.mazyfood.order.adapter.in.rest.order.payment;

public record ReceivePaymentRequestModel(int orderId, boolean authorized) {
}
