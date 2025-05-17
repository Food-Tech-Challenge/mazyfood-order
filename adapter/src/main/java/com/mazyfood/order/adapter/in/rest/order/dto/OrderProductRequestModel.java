package com.mazyfood.order.adapter.in.rest.order.dto;

import java.math.BigDecimal;

public record OrderProductRequestModel(int productId, String productName, BigDecimal price, int quantity) {

}
