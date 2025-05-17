package com.mazyfood.order.adapter.in.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.adapter.in.rest.order.dto.CreateOrderRequest;
import com.mazyfood.order.application.port.in.order.*;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@ContextConfiguration(classes = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @MockBean
    private GetOrderUseCase getOrderUseCase;

    @MockBean
    private GetAllOrdersUseCase getAllOrdersUseCase;

    @MockBean
    private GetOrderedOrdersUseCase getOrderedOrdersUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order(123);
        order.setId(new OrderId(1));
        order.setStatus(OrderStatus.INICIADO);

        CreateOrderRequest request = new CreateOrderRequest(Optional.of(123));

        when(createOrderUseCase.createOrder(123)).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(123))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetOrderByIdSuccess() throws Exception {
        Order order = new Order(456);
        order.setId(new OrderId(2));
        order.setStatus(OrderStatus.INICIADO);
        order.addProduct(10, "Pizza", new BigDecimal("25.00"), 2);

        when(getOrderUseCase.getOrder(new OrderId(2))).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(456))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.products.length()").value(1))
                .andExpect(jsonPath("$.products[0].id").value(10))
                .andExpect(jsonPath("$.products[0].name").value("Pizza"))
                .andExpect(jsonPath("$.products[0].quantity").value(2))
                .andExpect(jsonPath("$.products[0].price").value(25.00))
                .andExpect(jsonPath("$.products[0].total").value(50.00));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(getOrderUseCase.getOrder(new OrderId(99))).thenThrow(new OrderNotFoundException());

        mockMvc.perform(get("/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrders() throws Exception {
        Order o1 = new Order(1);
        o1.setId(new OrderId(101));
        Order o2 = new Order(2);
        o2.setId(new OrderId(102));

        when(getAllOrdersUseCase.getAllOrders()).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetOrderedProducts() throws Exception {
        Order o1 = new Order(1);
        o1.setId(new OrderId(201));
        Order o2 = new Order(2);
        o2.setId(new OrderId(202));

        when(getOrderedOrdersUseCase.getOrderedOrders()).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/orders/ordered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
