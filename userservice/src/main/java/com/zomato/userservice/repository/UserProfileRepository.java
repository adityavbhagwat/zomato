package com.zomato.userservice.repository;

import com.zomato.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // JpaRepository's findById(Long id) will now be used directly.
    UserProfile findByEmail(String email);
}