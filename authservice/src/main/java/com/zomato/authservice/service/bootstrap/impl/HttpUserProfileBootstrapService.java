package com.zomato.authservice.service.bootstrap.impl;

import com.zomato.authservice.dto.UserProfileBootstrapDTO;
import com.zomato.authservice.service.bootstrap.UserProfileBootstrapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpUserProfileBootstrapService implements UserProfileBootstrapService {

    @Value("${userservice.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void bootstrapUserProfile(UserProfileBootstrapDTO dto) {
        String url = userServiceUrl + "/bootstrap";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, dto, String.class);
            // Optionally handle response
        } catch (Exception e) {
            // print stack trace or log error
            e.printStackTrace();
        }
    }
}
