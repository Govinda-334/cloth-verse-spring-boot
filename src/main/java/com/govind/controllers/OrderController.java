package com.govind.controllers;

import com.govind.models.Order;
import com.govind.services.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Cart se order place karo
    @PostMapping("/place")
    public Order placeOrder(@RequestParam String address,
                            @RequestParam String paymentMode,
                            Authentication auth) {

        // auth.getName() = email (jwt subject)
        return orderService.placeOrderFromCart(
                auth.getName(),
                address,
                paymentMode
        );
    }

    // ✅ Logged-in user ke orders
    @GetMapping
    public List<Order> getUserOrders(Authentication auth) {
        return orderService.getUserOrders(auth.getName());
    }

    // ✅ Admin ke liye - sare orders
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // ✅ Admin - status update
    @PutMapping("/status/{orderId}")
    public Order updateStatus(@PathVariable Long orderId,
                              @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

}
