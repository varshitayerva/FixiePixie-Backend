package com.gl.project.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private Long userId;
    private Long serviceId;
    private LocalDate date;
    private String status;
    private String timeSlot;
}
