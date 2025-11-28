package com.eticaret.backend.service;

import com.eticaret.backend.dto.request.CreateOrderRequest;
import com.eticaret.backend.dto.response.OrderResponse;
import com.eticaret.backend.model.Order;

import java.util.List;

/**
 * Service interface for Order operations.
 */
public interface OrderService {

    /**
     * Create order from user's cart
     */
    OrderResponse createOrder(Long userId, CreateOrderRequest request);

    /**
     * Get order by id
     */
    OrderResponse getOrderById(Long orderId);

    /**
     * Get user's orders
     */
    List<OrderResponse> getUserOrders(Long userId);

    /**
     * Update order status
     */
    OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status);

    /**
     * Cancel order
     */
    void cancelOrder(Long orderId);
}
