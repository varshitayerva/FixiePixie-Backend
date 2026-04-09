package com.gl.app.PaymentMicroservice.Repository;

import com.gl.app.PaymentMicroservice.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentServiceRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findById(Long id);
    Optional<PaymentEntity> findByTransactionId(String transactionId);
    Optional<PaymentEntity> findByBookingId(String bookingId);

}
