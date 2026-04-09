package com.gl.app.PaymentMicroservice.Service;

import com.gl.app.PaymentMicroservice.Client.BookingClient;
import com.gl.app.PaymentMicroservice.DTO.PaymentDTO;
import com.gl.app.PaymentMicroservice.Entity.PaymentEntity;
import com.gl.app.PaymentMicroservice.Entity.PaymentStatus;
import com.gl.app.PaymentMicroservice.Repository.PaymentServiceRepository;
import com.gl.app.PaymentMicroservice.Utility.PaymentException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class PaymentServiceImpl implements PaymentServiceInterface {

    @Autowired
    private PaymentServiceRepository paymentServiceRepository;

    @Autowired
    private BookingClient bookingClient; // 1. Inject the BookingClient

    @Override
    public PaymentEntity processPayment(PaymentDTO paymentDTO) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setBookingId(String.valueOf(paymentDTO.getBookingId()));
        paymentEntity.setAmount(paymentDTO.getAmount());
        paymentEntity.setPaymentMethod(paymentDTO.getPaymentMethod());
        paymentEntity.setStatus(PaymentStatus.SUCCESS);
        PaymentEntity savedPayment = paymentServiceRepository.save(paymentEntity);

        // 2. AUTOMATION: Call Booking Service back
        // If the payment is successful, flip the booking status to CONFIRMED
        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            try {
                bookingClient.updateBookingStatus(paymentDTO.getBookingId(), "CONFIRMED");
            } catch (Exception e) {
                // We log the error but don't fail the payment process
                // because the money has already been "taken".
                System.err.println("CRITICAL: Payment succeeded but Booking Service update failed: " + e.getMessage());
            }
        }
        return savedPayment;
    }
    @Override
    public PaymentEntity getPaymentById(Long id) {
        return paymentServiceRepository.findById(id)
                .orElseThrow(() -> new PaymentException(HttpStatus.NOT_FOUND, "Payment not found with id: " + id));
    }

    @Override
    public PaymentEntity getPaymentByBookingId(Long bookingId) {
        return paymentServiceRepository.findByBookingId(String.valueOf(bookingId))
                .orElseThrow(() -> new PaymentException(HttpStatus.NOT_FOUND, "Payment not found with booking_id: " + bookingId));
    }

    @Override
    public PaymentEntity updatePaymentStatus(String transactionId, String status) {
        PaymentEntity payment = paymentServiceRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentException(HttpStatus.NOT_FOUND, "Payment not found for Transaction ID: " + transactionId));

        PaymentStatus nextStatus;
        try {
            nextStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new PaymentException(HttpStatus.BAD_REQUEST, "Invalid payment status: " + status, ex);
        }

        validateTransition(payment.getStatus(), nextStatus);
        payment.setStatus(nextStatus);
        return paymentServiceRepository.save(payment);
    }

    @Override
    public String getTransactionIdByPaymentId(Long paymentId){
        PaymentEntity payment = getPaymentById(paymentId);
        return payment.getTransactionId();
    }

    @Override
    public void refundPayment(String transactionId) {
        PaymentEntity payment = paymentServiceRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentException(HttpStatus.NOT_FOUND, "Payment not found for Transaction ID: " + transactionId));
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentException(HttpStatus.CONFLICT, "Only SUCCESS payments can be refunded.");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentServiceRepository.save(payment);

    }

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentServiceRepository.findAll();
    }

    private void validateTransition(PaymentStatus currentStatus, PaymentStatus nextStatus) {
        if (currentStatus == nextStatus) {
            throw new PaymentException(HttpStatus.CONFLICT, "Payment is already in status: " + nextStatus);
        }

        boolean valid = switch (currentStatus) {
            case PENDING -> nextStatus == PaymentStatus.SUCCESS || nextStatus == PaymentStatus.FAILED;
            case SUCCESS -> nextStatus == PaymentStatus.REFUNDED;
            case FAILED, REFUNDED -> false;
            default -> false;
        };

        if (!valid) {
            throw new PaymentException(
                    HttpStatus.CONFLICT,
                    "Invalid status transition: " + currentStatus + " -> " + nextStatus
            );
        }
    }
}
