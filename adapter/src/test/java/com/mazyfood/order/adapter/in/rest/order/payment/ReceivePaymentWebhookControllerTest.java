package com.mazyfood.order.adapter.in.rest.order.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.application.port.in.order.payment.ReceiveOrderPaymentUseCase;
import com.mazyfood.order.application.service.order.payment.OrderPaymentException;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReceivePaymentWebhookController.class)
@ContextConfiguration(classes = ReceivePaymentWebhookController.class)
class ReceivePaymentWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiveOrderPaymentUseCase receiveOrderPaymentUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testReceivePaymentSuccess() throws Exception {
        ReceivePaymentRequestModel request = new ReceivePaymentRequestModel(10, true);

        mockMvc.perform(post("/orders/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(receiveOrderPaymentUseCase).receivePayment(new OrderId(10), true);
    }

    @Test
    void testReceivePaymentFailure() throws Exception {
        ReceivePaymentRequestModel request = new ReceivePaymentRequestModel(99, true);

        doThrow(new OrderPaymentException("Pagamento inválido"))
                .when(receiveOrderPaymentUseCase).receivePayment(new OrderId(99), true);

        mockMvc.perform(post("/orders/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.orderId").value(99))
                .andExpect(jsonPath("$.message").value("Pagamento inválido"));
    }
}
