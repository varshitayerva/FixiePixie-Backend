package com.gl.project.BookingService.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String number;
    private String address;
}