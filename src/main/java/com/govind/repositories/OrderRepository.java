package com.govind.repositories;

import com.govind.models.Order;
import com.govind.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    // ❌ deleteOrder(Long orderId);  ---> YE PURA HATA DIYA
    // ✅ JpaRepository already deta hai:
    // deleteById(Long id)
}
