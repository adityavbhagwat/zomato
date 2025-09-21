package com.zomato.authservice.service;

import com.zomato.authservice.dto.UserRegistrationDTO;
import com.zomato.authservice.dto.UserLoginDTO;
import com.zomato.authservice.dto.UserResponseDTO;
import com.zomato.authservice.dto.LoginResponseDTO;
import com.zomato.authservice.model.User;
import com.zomato.authservice.repository.UserRepository;
import com.zomato.authservice.exception.AuthException;
import com.zomato.authservice.dto.UserProfileBootstrapDTO;
import com.zomato.authservice.service.bootstrap.UserProfileBootstrapService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
// imort logger 
import org.slf4j.LoggerFactory;

@Service
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileBootstrapService userProfileBootstrapService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthUserServiceImpl.class);

    @Override
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (registrationDTO.getEmail() == null || registrationDTO.getPassword() == null
                || registrationDTO.getName() == null) {
            throw new AuthException("Missing required fields", 400);
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()) != null) {
            throw new AuthException("Email already exists", 409);
        }
        try {
            User user = new User();
            user.setName(registrationDTO.getName());
            user.setEmail(registrationDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            User savedUser = userRepository.save(user);
            // Bootstrap user profile in user microservice

            UserProfileBootstrapDTO profileDTO = new UserProfileBootstrapDTO(savedUser.getId(), savedUser.getName(),
                    savedUser.getEmail());
            logger.debug("AuthService: Sending ProfileBootstrapDTO: userId={}, fullName={}, email={}",
                    profileDTO.getId(), profileDTO.getName(), profileDTO.getEmail());
            userProfileBootstrapService.bootstrapUserProfile(profileDTO);
            return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
        } catch (Exception e) {
            throw new AuthException("Registration failed: " + e.getMessage(), 500);
        }
    }

    @Override
    public LoginResponseDTO loginUser(UserLoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            throw new AuthException("Missing email or password", 400);
        }
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user == null) {
            throw new AuthException("Email not found", 404);
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid password", 401);
        }
        try {
            String token = jwtUtil.generateToken(user.getEmail());
            return new LoginResponseDTO(token, "Login successful");
        } catch (Exception e) {
            throw new AuthException("Login failed: " + e.getMessage(), 500);
        }
    }
}
