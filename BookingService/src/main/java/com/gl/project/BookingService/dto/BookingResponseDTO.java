package com.gl.project.BookingService.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingResponseDTO {

    private Long id;
    private Long userId;
    private Long serviceId;
    private LocalDate date;
    private String status;
}