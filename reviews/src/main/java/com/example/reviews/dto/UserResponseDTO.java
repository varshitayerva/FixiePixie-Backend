package com.example.reviews.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private boolean success;
    private String message;
    private UserData data;

    @Data
    public static class UserData {
        private String name;
    }
}
