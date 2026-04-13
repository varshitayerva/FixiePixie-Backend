package com.gl.project.UserService.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long userId;
    private String token;
    private String email;
    private String role;
    private String message;
}