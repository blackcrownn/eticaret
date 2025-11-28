package com.eticaret.backend.service.impl;

import com.eticaret.backend.dto.request.AddToCartRequest;
import com.eticaret.backend.dto.response.CartItemResponse;
import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.mapper.CartItemMapper;
import com.eticaret.backend.model.CartItem;
import com.eticaret.backend.model.Product;
import com.eticaret.backend.model.User;
import com.eticaret.backend.repository.CartItemRepository;
import com.eticaret.backend.repository.ProductRepository;
import com.eticaret.backend.repository.UserRepository;
import com.eticaret.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CartService.
 */
@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    public CartServiceImpl(CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(Long userId, AddToCartRequest request) {
        log.info("Adding product {} to cart for user {}", request.getProductId(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found with id: " + userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException("Product not found with id: " + request.getProductId()));

        // Check if product is active
        if (!product.getActive()) {
            throw new BusinessException("Product is not available");
        }

        // Check stock availability
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BusinessException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        CartItem cartItem;
        if (existingItem.isPresent()) {
            // Update quantity
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            if (product.getStockQuantity() < newQuantity) {
                throw new BusinessException("Insufficient stock. Available: " + product.getStockQuantity());
            }

            cartItem.setQuantity(newQuantity);
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
        }

        CartItem savedItem = cartItemRepository.save(cartItem);
        log.info("Cart item saved successfully");

        return cartItemMapper.toResponse(savedItem);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        log.info("Removing cart item {} for user {}", cartItemId, userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new BusinessException("Cart item does not belong to user");
        }

        cartItemRepository.delete(cartItem);
        log.info("Cart item removed successfully");
    }

    @Override
    @Transactional
    public CartItemResponse updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        log.info("Updating cart item {} quantity to {} for user {}", cartItemId, quantity, userId);

        if (quantity < 1) {
            throw new BusinessException("Quantity must be at least 1");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new BusinessException("Cart item does not belong to user");
        }

        // Check stock availability
        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new BusinessException("Insufficient stock. Available: " + cartItem.getProduct().getStockQuantity());
        }

        cartItem.setQuantity(quantity);
        CartItem updatedItem = cartItemRepository.save(cartItem);

        log.info("Cart item quantity updated successfully");
        return cartItemMapper.toResponse(updatedItem);
    }

    @Override
    public List<CartItemResponse> getCartItems(Long userId) {
        log.debug("Fetching cart items for user {}", userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        return cartItems.stream()
                .map(cartItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        log.info("Clearing cart for user {}", userId);

        cartItemRepository.deleteByUserId(userId);
        log.info("Cart cleared successfully");
    }
}
