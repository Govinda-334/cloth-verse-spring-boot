package com.govind.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.govind.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
