package com.govind.repositories;

import com.govind.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserServiceRepository extends JpaRepository<User, Long> {

    // Already exists
    User findByEmail(String email);

    // ✔ Admin ke liye simple all users fetch
    List<User> findAll();
}
