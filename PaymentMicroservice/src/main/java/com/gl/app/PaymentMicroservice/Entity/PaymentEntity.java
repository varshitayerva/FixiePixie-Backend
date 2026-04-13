package com.gl.app.PaymentMicroservice.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="Payment")
@Getter
@Setter
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "booking_id")
    private String bookingId;

    private Double amount;
    private String paymentMethod; // e.g., CARD, UPI


    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @PrePersist
    public void generateTransactionId() {
        // Generates a random unique ID like "TXN-87A2-B4C1"
        this.transactionId = "TXN-" + java.util.UUID.randomUUID().toString().toUpperCase().substring(0, 8);
    }

    @Enumerated(EnumType.STRING)
    private  PaymentStatus status;


}
