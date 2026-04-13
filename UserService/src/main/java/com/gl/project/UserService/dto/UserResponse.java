package com.gl.project.UserService.dto;

import com.gl.project.UserService.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String address;
    private long number;
    private Role role;
}