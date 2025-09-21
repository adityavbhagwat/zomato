package com.zomato.userservice.service;

import com.zomato.userservice.dto.UserProfileDTO;
import com.zomato.userservice.model.UserProfile;
import com.zomato.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger; // Add this import
import org.slf4j.LoggerFactory; // Add this import

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class); // Add this logger

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile createProfile(UserProfileDTO profileDTO) {
        UserProfile newProfile = new UserProfile(
                profileDTO.getId(),
                profileDTO.getName(),
                profileDTO.getEmail());

        // Log the UserProfile object before saving
        logger.debug("UserProfileService: Creating new UserProfile: userId={}, fullName={}, email={}",
                newProfile.getUserId(), newProfile.getName(), newProfile.getEmail());

        return userProfileRepository.save(newProfile);
    }
}