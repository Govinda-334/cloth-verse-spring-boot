package com.govind.services;

import java.util.List;
import com.govind.dto.AdminOrderResponse;
import com.govind.models.Order;

public interface OrderService {

    Order placeOrderFromCart(String email, String address, String paymentMode);

    List<Order> getUserOrders(String email);

    // ✅ RAW ORDERS (optional)
    List<Order> getAllOrders();

    // ✅ ✅ ADMIN DTO ORDERS
    List<AdminOrderResponse> getAllOrdersForAdmin();

    Order updateOrderStatus(Long orderId, String status);

    void deleteOrder(Long orderId);
}
