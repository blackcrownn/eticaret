package com.eticaret.backend.service.impl;

import com.eticaret.backend.dto.request.CreateOrderRequest;
import com.eticaret.backend.dto.response.OrderResponse;
import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.mapper.OrderMapper;
import com.eticaret.backend.model.CartItem;
import com.eticaret.backend.model.Order;
import com.eticaret.backend.model.Product;
import com.eticaret.backend.model.User;
import com.eticaret.backend.repository.CartItemRepository;
import com.eticaret.backend.repository.OrderRepository;
import com.eticaret.backend.repository.ProductRepository;
import com.eticaret.backend.repository.UserRepository;
import com.eticaret.backend.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService.
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
            UserRepository userRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        log.info("Creating order for user {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found with id: " + userId));

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        // Calculate total amount and validate stock
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            // Check stock availability
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }

            // Calculate subtotal
            BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(Order.OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        // Reduce stock quantities
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Clear cart
        cartItemRepository.deleteByUserId(userId);

        log.info("Order created successfully with id: {}", savedOrder.getId());
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        log.debug("Fetching order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getUserOrders(Long userId) {
        log.debug("Fetching orders for user {}", userId);

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status) {
        log.info("Updating order {} status to {}", orderId, status);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        log.info("Order status updated successfully");
        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        log.info("Cancelling order {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new BusinessException("Cannot cancel delivered order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled successfully");
    }
}
