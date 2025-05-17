package com.mazyfood.order.adapter.in.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.adapter.in.rest.order.dto.OrderRequestModel;
import com.mazyfood.order.application.port.in.order.UpdateOrderStatusUseCase;
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

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UpdateOrderStatusController.class)
@ContextConfiguration(classes = UpdateOrderStatusController.class)
class UpdateOrderStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUpdateOrderStatusSuccess() throws Exception {
        Order order = new Order(123);
        order.setId(new OrderId(1));
        order.setStatus(OrderStatus.RECEBIDO);

        OrderRequestModel requestModel = new OrderRequestModel("RECEBIDO");

        when(updateOrderStatusUseCase.updateStatus(new OrderId(1), "RECEBIDO"))
                .thenReturn(Optional.of(order));

        mockMvc.perform(patch("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(123))
                .andExpect(jsonPath("$.orderStatus").value("RECEBIDO"));
    }

    @Test
    void testUpdateOrderStatusNotFound() throws Exception {
        OrderRequestModel requestModel = new OrderRequestModel("RECEBIDO");

        when(updateOrderStatusUseCase.updateStatus(new OrderId(99), "RECEBIDO"))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/orders/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel)))
                .andExpect(status().isNotFound());
    }
}
