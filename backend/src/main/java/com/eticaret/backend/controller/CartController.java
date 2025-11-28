package com.eticaret.backend.controller;

import com.eticaret.backend.dto.request.AddToCartRequest;
import com.eticaret.backend.dto.response.CartItemResponse;
import com.eticaret.backend.service.CartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Cart operations.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(@PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request) {
        log.info("Adding item to cart for user: {}", userId);
        CartItemResponse response = cartService.addToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable Long userId) {
        log.debug("Fetching cart items for user: {}", userId);
        List<CartItemResponse> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateQuantity(@PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        log.info("Updating cart item quantity for user: {}", userId);
        CartItemResponse response = cartService.updateQuantity(userId, cartItemId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId,
            @PathVariable Long cartItemId) {
        log.info("Removing item from cart for user: {}", userId);
        cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        log.info("Clearing cart for user: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
