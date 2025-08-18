package com.zomato.authservice.service;

import com.zomato.authservice.dto.UserRegistrationDTO;
import com.zomato.authservice.dto.UserLoginDTO;
import com.zomato.authservice.dto.UserResponseDTO;
import com.zomato.authservice.dto.LoginResponseDTO;
import com.zomato.authservice.model.User;
import com.zomato.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public LoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getEmail());
            return new LoginResponseDTO(token, "Login successful");
        }
        return new LoginResponseDTO(null, "Invalid credentials");
    }
}
