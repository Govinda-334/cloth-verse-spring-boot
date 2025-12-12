package com.govind.services;

import com.govind.dto.UserResponseDTO;
import com.govind.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User updatedData);

    void deleteUser(Long id);

    List<UserResponseDTO> getAllUsersForAdmin();
}
