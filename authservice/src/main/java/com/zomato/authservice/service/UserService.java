package com.zomato.authservice.service;

import com.zomato.authservice.dto.UserRegistrationDTO;
import com.zomato.authservice.dto.UserLoginDTO;
import com.zomato.authservice.model.User;

import com.zomato.authservice.dto.UserResponseDTO;
import com.zomato.authservice.dto.LoginResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO registrationDTO);

    LoginResponseDTO loginUser(UserLoginDTO loginDTO);
}
