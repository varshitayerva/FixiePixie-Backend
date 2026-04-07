package com.gl.project.UserService.service;

import com.gl.project.UserService.config.JwtUtil;
import com.gl.project.UserService.dto.*;
import com.gl.project.UserService.entity.*;

import com.gl.project.UserService.repository.UserRepository;
import com.gl.project.UserService.utility.UserException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Automatically injects all final fields via constructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDTO register(UserRegisterDTO dto) throws UserException {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserException("Email already registered");
        }

        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO dto) throws UserException {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return LoginResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
