package com.govind.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.govind.services.CartService;

@RestController
@RequestMapping("/api/user/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //  ADD TO CART
    @PostMapping("/add/{productId}")
    public Object addToCart(@PathVariable Long productId, Authentication auth) {
        return cartService.addToCart(auth.getName(), productId);
    }

    //  GET USER CART
    @GetMapping
    public Object getUserCart(Authentication auth) {
        return cartService.getUserCart(auth.getName());
    }

    //  REMOVE ITEM
    @DeleteMapping("/remove/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
    }
 // INCREASE QUANTITY
    @PutMapping("/increase/{itemId}")
    public Object increase(@PathVariable Long itemId) {
        return cartService.increaseQuantity(itemId);
    }

    //  DECREASE QUANTITY
    @PutMapping("/decrease/{itemId}")
    public Object decrease(@PathVariable Long itemId) {
        return cartService.decreaseQuantity(itemId);
    }

}
