package com.mazyfood.order.adapter.in.rest.order.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazyfood.order.application.port.in.order.payment.PayOrderUseCase;
import com.mazyfood.order.application.service.order.payment.OrderPaymentException;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PayOrderController.class)
@ContextConfiguration(classes = PayOrderController.class)
class PayOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayOrderUseCase payOrderUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessPaymentSuccess() throws Exception {
        PaymentRequestModel request = new PaymentRequestModel("pix");

        when(payOrderUseCase.processPayment(new OrderId(1), "pix"))
                .thenReturn("Pagamento processado com sucesso");

        mockMvc.perform(post("/orders/1/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("Pagamento processado com sucesso"));
    }

    @Test
    void testProcessPaymentFailure() throws Exception {
        PaymentRequestModel request = new PaymentRequestModel("credit_card");

        when(payOrderUseCase.processPayment(new OrderId(1), "credit_card"))
                .thenThrow(new OrderPaymentException("Pagamento recusado"));

        mockMvc.perform(post("/orders/1/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.paymentStatus").value("Pagamento recusado"));
    }
}
