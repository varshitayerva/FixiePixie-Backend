package com.gl.project.BookingService.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private String email;
    private String phoneNumber;
    private String subject;
    private String message;
}