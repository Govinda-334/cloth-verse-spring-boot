package com.govind.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.govind.models.Cart;
import com.govind.models.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
