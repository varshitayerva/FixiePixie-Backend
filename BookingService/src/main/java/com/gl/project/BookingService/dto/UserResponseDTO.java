package com.gl.project.BookingService.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private boolean success;
    private String message;
    private UserDTO data;
}