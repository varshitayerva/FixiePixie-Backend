package com.gl.project.UserService.service;
import com.gl.project.UserService.dto.*;
import com.gl.project.UserService.utility.UserException;

import java.util.List;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO dto) throws UserException;

    LoginResponseDTO login(UserLoginDTO dto) throws UserException;

    UserResponseDTO getUserById(Long id) throws UserException;

    List<UserResponseDTO> getAllUsers();

    void deleteUser(Long id) throws UserException;
}
