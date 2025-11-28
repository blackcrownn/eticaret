package com.eticaret.backend.service;

import com.eticaret.backend.dto.request.AddToCartRequest;
import com.eticaret.backend.dto.response.CartItemResponse;

import java.util.List;

/**
 * Service interface for Cart operations.
 */
public interface CartService {

    /**
     * Add item to cart
     */
    CartItemResponse addToCart(Long userId, AddToCartRequest request);

    /**
     * Remove item from cart
     */
    void removeFromCart(Long userId, Long cartItemId);

    /**
     * Update cart item quantity
     */
    CartItemResponse updateQuantity(Long userId, Long cartItemId, Integer quantity);

    /**
     * Get user's cart items
     */
    List<CartItemResponse> getCartItems(Long userId);

    /**
     * Clear user's cart
     */
    void clearCart(Long userId);
}
