package com.govind.services;

import com.govind.dto.AdminOrderResponse;
import com.govind.models.*;
import com.govind.repositories.CartRepository;
import com.govind.repositories.OrderRepository;
import com.govind.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;

    public OrderServiceImpl(CartRepository cartRepo,
                            UserRepository userRepo,
                            OrderRepository orderRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    // ✅ USER PLACE ORDER
    @Override
    public Order placeOrderFromCart(String email, String address, String paymentMode) {

        User user = userRepo.findByEmail(email).orElseThrow();

        Cart cart = cartRepo.findByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setUser(user);
        order.setAddress(address);
        order.setPaymentMode(paymentMode);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem item = new OrderItem();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getProduct().getPrice());
            item.setOrder(order);

            BigDecimal itemTotal =
                    cartItem.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            total = total.add(itemTotal);
            orderItems.add(item);
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order savedOrder = orderRepo.save(order);

        cart.getItems().clear();
        cartRepo.save(cart);

        return savedOrder;
    }

    // ✅ USER ORDERS
    @Override
    public List<Order> getUserOrders(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return orderRepo.findByUser(user);
    }

    // ✅ ADMIN ALL ORDERS (RAW)
    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public List<AdminOrderResponse> getAllOrdersForAdmin() {

        List<Order> orders = orderRepo.findAll();

        return orders.stream().map(order -> {

            AdminOrderResponse dto = new AdminOrderResponse();
            dto.setId(order.getId());
            dto.setStatus(order.getStatus().name());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setAddress(order.getAddress());

          


            // 🔥 FIRST PRODUCT FOR IMAGE + NAME
            if (!order.getItems().isEmpty()) {
                OrderItem first = order.getItems().get(0);
                dto.setProductName(first.getProduct().getName());
                dto.setProductImage(first.getProduct().getImagePath());
            }

            // 🔥 TOTAL QUANTITY OF ALL ITEMS
            int totalQty = order.getItems()
                    .stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
            dto.setTotalQuantity(totalQty);

            // 🔥 USER DETAILS
            if (order.getUser() != null) {
                dto.setUserName(order.getUser().getName());
                dto.setUserPhone(order.getUser().getPhone());
            }

            return dto;

        }).toList();
    }


    // 
    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.valueOf(status));
        return orderRepo.save(order);
    }

    // ✅ ADMIN DELETE ORDER
    @Override
    public void deleteOrder(Long orderId) {
        orderRepo.deleteById(orderId);
    }
}
