package com.govind.services;

import org.springframework.stereotype.Service;

import com.govind.models.User;
import com.govind.repositories.UserRepository;

@Service
public class UserProfileService {

    private final UserRepository userRepo;

    public UserProfileService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // GET USER PROFILE
    public User getProfile(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // UPDATE USER PROFILE
    public User updateProfile(String email, User updatedUser) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedUser.getName());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());

        return userRepo.save(user);
    }
}
