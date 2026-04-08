package com.gl.app.PaymentMicroservice.Service;

import com.gl.app.PaymentMicroservice.DTO.PaymentDTO;
import com.gl.app.PaymentMicroservice.Entity.PaymentEntity;
import com.gl.app.PaymentMicroservice.Entity.PaymentStatus;
import com.gl.app.PaymentMicroservice.Repository.PaymentServiceRepository;
import com.gl.app.PaymentMicroservice.Utility.PaymentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentServiceRepository paymentServiceRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void processPaymentShouldCreatePendingPayment() {
        PaymentDTO dto = PaymentDTO.builder()
                .bookingId(9001L)
                .amount(149.99)
                .paymentMethod("UPI")
                .build();

        PaymentEntity saved = new PaymentEntity();
        saved.setPaymentId(1L);
        saved.setBookingId("9001");
        saved.setAmount(149.99);
        saved.setPaymentMethod("UPI");
        saved.setStatus(PaymentStatus.PENDING);
        saved.setTransactionId("TXN-12345678");

        when(paymentServiceRepository.save(any(PaymentEntity.class))).thenReturn(saved);

        PaymentEntity result = paymentService.processPayment(dto);

        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals("9001", result.getBookingId());
        verify(paymentServiceRepository).save(any(PaymentEntity.class));
    }

    @Test
    void getPaymentByIdShouldReturnPayment() {
        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentId(1L);
        payment.setTransactionId("TXN-123");
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentServiceRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentEntity result = paymentService.getPaymentById(1L);

        assertEquals(1L, result.getPaymentId());
        assertEquals(PaymentStatus.PENDING, result.getStatus());
    }

    @Test
    void getPaymentByIdShouldThrowWhenMissing() {
        when(paymentServiceRepository.findById(99L)).thenReturn(Optional.empty());

        PaymentException ex = assertThrows(PaymentException.class, () -> paymentService.getPaymentById(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void updatePaymentStatusShouldAllowPendingToSuccess() {
        PaymentEntity payment = new PaymentEntity();
        payment.setTransactionId("TXN-123");
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentServiceRepository.findByTransactionId("TXN-123")).thenReturn(Optional.of(payment));
        when(paymentServiceRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentEntity result = paymentService.updatePaymentStatus("TXN-123", "SUCCESS");

        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        verify(paymentServiceRepository).save(payment);
    }

    @Test
    void updatePaymentStatusShouldRejectInvalidTransition() {
        PaymentEntity payment = new PaymentEntity();
        payment.setTransactionId("TXN-123");
        payment.setStatus(PaymentStatus.FAILED);

        when(paymentServiceRepository.findByTransactionId("TXN-123")).thenReturn(Optional.of(payment));

        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> paymentService.updatePaymentStatus("TXN-123", "SUCCESS")
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void refundPaymentShouldOnlyWorkForSuccess() {
        PaymentEntity payment = new PaymentEntity();
        payment.setTransactionId("TXN-123");
        payment.setStatus(PaymentStatus.SUCCESS);

        when(paymentServiceRepository.findByTransactionId("TXN-123")).thenReturn(Optional.of(payment));
        when(paymentServiceRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.refundPayment("TXN-123");

        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
        verify(paymentServiceRepository).save(payment);
    }

    @Test
    void refundPaymentShouldRejectNonSuccessPayments() {
        PaymentEntity payment = new PaymentEntity();
        payment.setTransactionId("TXN-123");
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentServiceRepository.findByTransactionId("TXN-123")).thenReturn(Optional.of(payment));

        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> paymentService.refundPayment("TXN-123")
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void getAllPaymentsShouldReturnList() {
        when(paymentServiceRepository.findAll()).thenReturn(List.of(new PaymentEntity()));

        List<PaymentEntity> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
    }
}
