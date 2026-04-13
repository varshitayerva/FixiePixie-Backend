package com.gl.app.PaymentMicroservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
    @NotNull
    private Long bookingId;

    @NotNull
    private String status;

    @NotNull
    private Double amount;     // Total price to be charged

    @NotBlank
    private String paymentMethod; // e.g., "CREDIT_CARD", "UPI", "WALLET"

}