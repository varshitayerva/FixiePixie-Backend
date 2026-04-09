package com.gl.app.PaymentMicroservice.Service;

import com.gl.app.PaymentMicroservice.DTO.PaymentDTO;
import com.gl.app.PaymentMicroservice.Entity.PaymentEntity;

import java.util.List;


public interface PaymentServiceInterface {
    PaymentEntity processPayment(PaymentDTO paymentDTO);
    PaymentEntity getPaymentById(Long id);
    PaymentEntity getPaymentByBookingId(Long bookingId);
    PaymentEntity updatePaymentStatus(String transactionId, String status);
    void refundPayment(String transactionId);
    List<PaymentEntity> getAllPayments();
    String getTransactionIdByPaymentId(Long paymentId);


}
