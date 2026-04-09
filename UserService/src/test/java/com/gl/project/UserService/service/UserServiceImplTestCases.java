package com.gl.project.UserService.service;

import com.gl.project.UserService.dto.*;
import com.gl.project.UserService.entity.Role;
import com.gl.project.UserService.entity.User;
import com.gl.project.UserService.repository.UserRepository;
import com.gl.project.UserService.security.CustomUserDetailsService;
import com.gl.project.UserService.security.JwtService;
import com.gl.project.UserService.utility.ResourceNotFoundException;
import com.gl.project.UserService.utility.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceImplTestCases {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void register_shouldCreateUser() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Sai");
        request.setEmail("test@test.com");
        request.setPassword("123456");
        request.setAddress("Chennai");
        request.setNumber(9876543210L);
        request.setRole(Role.ROLE_USER);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        User savedUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.save(any())).thenReturn(savedUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(jwtService.generateToken(any())).thenReturn("token");

        AuthResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());
    }


    @Test
    void register_shouldThrowException_ifUserExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.com");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(request));
    }


    @Test
    void getUserById_shouldReturnUser() {

        User user = User.builder().id(1L).email("test@test.com").build();
        UserResponse response = new UserResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(response);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
    }


    @Test
    void deleteUser_shouldDeleteUser() {

        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }
}