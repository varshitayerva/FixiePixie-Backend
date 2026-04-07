package com.gl.project.UserService.controller;


import com.gl.project.UserService.dto.*;
import com.gl.project.UserService.service.UserService;

import com.gl.project.UserService.utility.UserException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterDTO dto) throws UserException {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody UserLoginDTO dto) throws UserException {

        return ResponseEntity.ok(userService.login(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) throws UserException{
        return ResponseEntity.ok("Fetch user by ID: " + id);
    }
}