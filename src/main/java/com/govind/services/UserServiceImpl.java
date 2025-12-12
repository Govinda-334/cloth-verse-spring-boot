package com.govind.services;

import com.govind.dto.UserResponseDTO;
import com.govind.models.User;
import com.govind.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
    }

    @Override
    public User updateUser(Long id, User updatedData) {
        User user = getUserById(id);

        if (updatedData.getName() != null)
            user.setName(updatedData.getName());

        if (updatedData.getPhone() != null)
            user.setPhone(updatedData.getPhone());

        if (updatedData.getAddress() != null)
            user.setAddress(updatedData.getAddress());

        // ⭐ Required for admin role update
        if (updatedData.getRole() != null)
            user.setRole(updatedData.getRole());

        // ❌ active update intentionally removed

        return userRepo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public List<UserResponseDTO> getAllUsersForAdmin() {
        List<User> users = userRepo.findAll();

        return users.stream().map(user -> {
            UserResponseDTO dto = new UserResponseDTO();

            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAddress(user.getAddress());
            dto.setRole(user.getRole().name());

          

            return dto;
        }).toList();
    }
}
