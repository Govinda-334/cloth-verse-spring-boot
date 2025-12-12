package com.govind.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.govind.dto.AdminOrderResponse;
import com.govind.services.OrderService;
@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin("*")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ ✅ ADMIN: ALL ORDERS WITH USER NAME + PHONE
    @GetMapping
    public List<AdminOrderResponse> getAllOrders() {
        return orderService.getAllOrdersForAdmin();
    }

    // ✅ ADMIN: Status update
    @PutMapping("/status/{orderId}")
    public void updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status
    ) {
        orderService.updateOrderStatus(orderId, status);
    }

    // ✅ ADMIN: Delete order
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
