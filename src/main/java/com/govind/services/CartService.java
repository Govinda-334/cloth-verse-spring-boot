package com.govind.services;

import org.springframework.stereotype.Service;
import com.govind.models.*;
import com.govind.repositories.*;

import java.util.*;
@Service
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public CartService(CartRepository cartRepo,
                       CartItemRepository cartItemRepo,
                       ProductRepository productRepo,
                       UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public Cart addToCart(String email, Long productId) {

        User user = userRepo.findByEmail(email).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
        }

        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(1);
            item.setCart(cart);
            cart.getItems().add(item);
        }

        return cartRepo.save(cart);
    }

    public Cart getUserCart(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return cartRepo.findByUser(user);
    }

    public void removeItem(Long itemId) {
        cartItemRepo.deleteById(itemId);
    }
    public Cart increaseQuantity(Long itemId) {
        CartItem item = cartItemRepo.findById(itemId).orElseThrow();
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepo.save(item);
        return cartRepo.findById(item.getCart().getId()).orElseThrow();
    }

    public Cart decreaseQuantity(Long itemId) {
        CartItem item = cartItemRepo.findById(itemId).orElseThrow();

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepo.save(item);
        }

        return cartRepo.findById(item.getCart().getId()).orElseThrow();
    }

}
