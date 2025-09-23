package com.zomato.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserProfile {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "address")
    private String address;

    public UserProfile() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor to be used by createProfile, now takes 'id' (which is
    // authservice's userId)
    public UserProfile(Long id, String name, String email) {
        this.id = id; // Set the ID directly
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    } // Setter for ID
      // Removed getUserId() and setUserId() as 'id' now serves this purpose

    public String getName() {
        return name;
    }

    public void setName(String fullName) {
        this.name = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}