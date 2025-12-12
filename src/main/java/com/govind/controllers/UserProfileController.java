package com.govind.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.govind.models.User;
import com.govind.services.UserProfileService;

@RestController
@RequestMapping("/api/user/profile")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService profileService;

    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    
    @GetMapping
    public User getProfile(Authentication auth) {
        return profileService.getProfile(auth.getName());
    }

    // ✅ UPDATE PROFILE
    @PutMapping
    public User updateProfile(
            @RequestBody User user,
            Authentication auth) {

        return profileService.updateProfile(auth.getName(), user);
    }
}
