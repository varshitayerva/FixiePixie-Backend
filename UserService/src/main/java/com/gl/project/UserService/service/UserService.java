package com.gl.project.UserService.service;
import com.gl.project.UserService.dto.*;
import com.gl.project.UserService.utility.UserException;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO dto) throws UserException;

    LoginResponseDTO login(UserLoginDTO dto) throws UserException;
}
