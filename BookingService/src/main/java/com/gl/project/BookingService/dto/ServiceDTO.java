package com.gl.project.BookingService.dto;

import lombok.Data;

@Data
public class ServiceDTO {
    private Long id;
    private String serviceName;
    private double price;
    private String description;
    private String category;
}
