package com.zomato.authservice.controller;

import com.zomato.authservice.dto.LoginResponseDTO;
import com.zomato.authservice.dto.UserLoginDTO;
import com.zomato.authservice.dto.UserRegistrationDTO;
import com.zomato.authservice.dto.UserResponseDTO;
import com.zomato.authservice.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO response = authUserService.registerUser(registrationDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody UserLoginDTO loginDTO) {
        LoginResponseDTO response = authUserService.loginUser(loginDTO);
        if (response.getToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }
}
