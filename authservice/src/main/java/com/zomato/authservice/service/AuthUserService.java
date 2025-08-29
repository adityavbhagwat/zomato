package com.zomato.authservice.service;

import com.zomato.authservice.dto.UserRegistrationDTO;
import com.zomato.authservice.dto.UserLoginDTO;

import com.zomato.authservice.dto.UserResponseDTO;
import com.zomato.authservice.dto.LoginResponseDTO;

public interface AuthUserService {
    UserResponseDTO registerUser(UserRegistrationDTO registrationDTO);

    LoginResponseDTO loginUser(UserLoginDTO loginDTO);
}
