package com.mazyfood.order.adapter.in.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.adapter.in.rest.order.dto.OrderProductRequestModel;
import com.mazyfood.order.application.port.in.order.AddProductToOrderUseCase;
import com.mazyfood.order.application.port.in.order.OrderNotFoundException;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddProductToOrderController.class)
@ContextConfiguration(classes = AddProductToOrderController.class)
class AddProductToOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddProductToOrderUseCase addProductToOrderUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddProductToOrderSuccess() throws Exception {
        OrderProductRequestModel requestModel = new OrderProductRequestModel(10, "Pizza", new BigDecimal("25.00"), 2);
        Order order = new Order(123);
        order.setId(new OrderId(1));
        order.setStatus(OrderStatus.INICIADO);

        when(addProductToOrderUseCase.addProductToOrder(any(), eq(10), eq("Pizza"), eq(new BigDecimal("25.00")), eq(2)))
                .thenReturn(order);

        mockMvc.perform(put("/orders/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(123))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testAddProductToOrderReturnsNotFound() throws Exception {
        OrderProductRequestModel requestModel = new OrderProductRequestModel(10, "Pizza", new BigDecimal("25.00"), 2);

        when(addProductToOrderUseCase.addProductToOrder(any(), anyInt(), anyString(), any(), anyInt()))
                .thenThrow(new OrderNotFoundException());

        mockMvc.perform(put("/orders/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel)))
                .andExpect(status().isNotFound());
    }
}
