package com.gl.project.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsDTO {

    private Long id;
    private String userName;
    private String serviceName;
    private Long serviceId;

    private LocalDate date;
    private String status;
    private String timeSlot;
}