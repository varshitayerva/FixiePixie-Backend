package com.gl.app.PaymentMicroservice.Controller;

import com.gl.app.PaymentMicroservice.DTO.PaymentDTO;
import com.gl.app.PaymentMicroservice.Entity.PaymentEntity;
import com.gl.app.PaymentMicroservice.Service.PaymentServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class Controller {
    @Autowired
    private PaymentServiceInterface paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentEntity> processPayment(
            @Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentEntity payment = paymentService.processPayment(paymentDTO);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentEntity> getPaymentByBookingId(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

    @PutMapping("/status")
    public ResponseEntity<PaymentEntity> updatePaymentStatus(
            @RequestParam String transactionId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                paymentService.updatePaymentStatus(transactionId, status)
        );
    }

    @GetMapping("/{paymentId}/transaction-id")
    public ResponseEntity<String> getTransactionId(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getTransactionIdByPaymentId(paymentId)
        );
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refundPayment(
            @RequestParam String transactionId) {

        paymentService.refundPayment(transactionId);
        return ResponseEntity.ok("Payment refunded successfully");
    }

    @GetMapping
    public ResponseEntity<List<PaymentEntity>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }


}

