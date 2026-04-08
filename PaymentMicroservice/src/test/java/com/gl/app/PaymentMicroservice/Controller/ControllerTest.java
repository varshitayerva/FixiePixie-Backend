package com.gl.app.PaymentMicroservice.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.app.PaymentMicroservice.DTO.PaymentDTO;
import com.gl.app.PaymentMicroservice.Entity.PaymentEntity;
import com.gl.app.PaymentMicroservice.Entity.PaymentStatus;
import com.gl.app.PaymentMicroservice.Service.PaymentServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentServiceInterface paymentService;

    @Test
    void processPaymentShouldReturnCreatedPayment() throws Exception {
        PaymentDTO dto = PaymentDTO.builder()
                .bookingId(9001L)
                .amount(149.99)
                .paymentMethod("UPI")
                .build();

        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentId(1L);
        payment.setBookingId("9001");
        payment.setAmount(149.99);
        payment.setPaymentMethod("UPI");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-12345678");

        when(paymentService.processPayment(any(PaymentDTO.class))).thenReturn(payment);

        mockMvc.perform(post("/payment/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.transactionId").value("TXN-12345678"));
    }

    @Test
    void updatePaymentStatusShouldReturnUpdatedPayment() throws Exception {
        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentId(1L);
        payment.setTransactionId("TXN-12345678");
        payment.setStatus(PaymentStatus.SUCCESS);

        when(paymentService.updatePaymentStatus("TXN-12345678", "SUCCESS")).thenReturn(payment);

        mockMvc.perform(put("/payment/status")
                        .param("transactionId", "TXN-12345678")
                        .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void refundPaymentShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post("/payment/refund")
                        .param("transactionId", "TXN-12345678"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment refunded successfully"));
    }

    @Test
    void getAllPaymentsShouldReturnList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of(new PaymentEntity()));

        mockMvc.perform(get("/payment"))
                .andExpect(status().isOk());
    }
}
