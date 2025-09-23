package com.zomato.userservice.controller;

import com.zomato.userservice.dto.UserProfileDTO;
import com.zomato.userservice.dto.UpdateUserProfileDTO; // New import
import com.zomato.userservice.model.UserProfile;
import com.zomato.userservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // New import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // New import
import org.springframework.security.oauth2.jwt.Jwt; // New import

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

    @PatchMapping("/{id}") // Changed path variable name to 'id'
    public ResponseEntity<UserProfile> updateProfile(
            @PathVariable Long id, // Changed parameter name to 'id'
            @RequestBody UpdateUserProfileDTO updateDTO,
            @AuthenticationPrincipal Jwt jwt) {

        String authenticatedUserEmail = jwt.getSubject();
        // fetch the id of the email from the database and assign it to
        // authenticatedUserId variable
        Long authenticatedUserId = userProfileService.getUserIdByEmail(authenticatedUserEmail);

        // get userid from the database based on the authenticatedUserEmail not from the
        // jwt

        logger.info("UserProfileController: Authenticated user ID (from JWT sub claim): {}", authenticatedUserId);
        logger.info("UserProfileController: Authenticated user Email (from custom JWT claim): {}",
                authenticatedUserEmail);
        logger.debug("UserProfileController: Attempting to update profile for path id: {}", id);

        // Authorization check: Ensure the authenticated user's ID matches the profile
        // ID being updated
        if (!authenticatedUserId.equals(id)) {
            logger.warn("Unauthorized attempt to update profile for id {} by authenticated user ID {}", id,
                    authenticatedUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        logger.debug("UserProfileController: Received update request for id {}: fullName={}, email={}, address={}",
                id, updateDTO.getName(), updateDTO.getEmail(), updateDTO.getAddress());

        try {
            UserProfile updatedProfile = userProfileService.updateUserProfile(id, updateDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            logger.error("Error updating profile for id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}