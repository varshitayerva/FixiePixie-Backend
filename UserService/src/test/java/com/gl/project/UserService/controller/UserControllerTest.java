package com.gl.project.UserService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.project.UserService.dto.*;
import com.gl.project.UserService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void registerUser() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setName("Sai");
        request.setEmail("test@test.com");
        request.setPassword("123456");
        request.setAddress("Chennai");
        request.setNumber(9876543210L);

        when(userService.register(Mockito.any()))
                .thenReturn(new AuthResponse());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    @Test
    void loginUser() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("123");

        when(userService.login(Mockito.any()))
                .thenReturn(new AuthResponse());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById() throws Exception {

        when(userService.getUserById(1L))
                .thenReturn(new UserResponse());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }


    @Test
    void getAllUsers() throws Exception {

        when(userService.getAllUsers())
                .thenReturn(Collections.singletonList(new UserResponse()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }


    @Test
    void deleteUser() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
