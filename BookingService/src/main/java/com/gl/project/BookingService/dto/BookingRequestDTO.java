package com.gl.project.BookingService.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequestDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long serviceId;

    @NotNull
    @Future(message = "Booking date must be in future")
    private LocalDate date;
}