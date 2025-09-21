package com.zomato.userservice.controller;

import com.zomato.userservice.dto.UserProfileDTO;
import com.zomato.userservice.model.UserProfile;
import com.zomato.userservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/")
    public String healthCheck() {
        return "User Profile Service is running!";
    }

    @PostMapping("/bootstrap")
    public ResponseEntity<UserProfile> bootstrapProfile(@RequestBody UserProfileDTO profileDTO) {
        logger.debug("UserProfileController: Received bootstrap request: userId={}, fullName={}, email={}",
                profileDTO.getId(), profileDTO.getName(), profileDTO.getEmail());

        UserProfile createdProfile = userProfileService.createProfile(profileDTO);
        return ResponseEntity.ok(createdProfile);
    }

    // You can add other profile-related endpoints here later, e.g.,
    // @GetMapping("/{userId}")
    // public ResponseEntity<UserProfile> getProfile(@PathVariable Long userId) {
    // ... }
}