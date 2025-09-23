package com.zomato.userservice.service;

import com.zomato.userservice.dto.UserProfileDTO;
import com.zomato.userservice.dto.UpdateUserProfileDTO; // New import
import com.zomato.userservice.model.UserProfile;
import com.zomato.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional; // New import

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile createProfile(UserProfileDTO profileDTO) {
        UserProfile newProfile = new UserProfile(
                profileDTO.getId(),
                profileDTO.getName(),
                profileDTO.getEmail());

        // Log the UserProfile object before saving
        logger.debug("UserProfileService: Creating new UserProfile: userId={}, fullName={}, email={}",
                newProfile.getId(), newProfile.getName(), newProfile.getEmail());

        return userProfileRepository.save(newProfile);
    }

    // New method to update user profile
    public UserProfile updateUserProfile(Long userId, UpdateUserProfileDTO updateDTO) {
        Optional<UserProfile> existingProfileOptional = userProfileRepository.findById(userId);

        if (existingProfileOptional.isEmpty()) {
            logger.warn("UserProfileService: Profile not found for userId: {}", userId);
            throw new RuntimeException("User profile not found for ID: " + userId); // Or a custom exception
        }

        UserProfile existingProfile = existingProfileOptional.get();

        // Apply updates only for non-null fields
        if (updateDTO.getName() != null) {
            existingProfile.setName(updateDTO.getName());
        }
        if (updateDTO.getEmail() != null) {
            existingProfile.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getAddress() != null) {
            existingProfile.setAddress(updateDTO.getAddress());
        }

        logger.info("UserProfileService: Updating profile for userId {}. New data: fullName={}, email={}, address={}",
                userId, existingProfile.getName(), existingProfile.getEmail(), existingProfile.getAddress());

        return userProfileRepository.save(existingProfile);
    }

    // New method to getUserIdByEmail
    public Long getUserIdByEmail(String email) {
        return userProfileRepository.findByEmail(email).getId();
    }
}