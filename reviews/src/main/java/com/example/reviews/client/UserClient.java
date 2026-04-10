package com.example.reviews.client;


import com.example.reviews.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable("id") Long id); // Returns user name or status
}
