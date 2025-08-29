package com.zomato.authservice.service.bootstrap;

import com.zomato.authservice.dto.UserProfileBootstrapDTO;

public interface UserProfileBootstrapService {
    void bootstrapUserProfile(UserProfileBootstrapDTO dto);
}
