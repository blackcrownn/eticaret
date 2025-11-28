package com.eticaret.backend.service.impl;

import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.model.Order;
import com.eticaret.backend.repository.OrderRepository;
import com.eticaret.backend.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PaymentService.
 * Simplified implementation - in production, integrate with real payment
 * gateway
 */
@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderRepository orderRepository;

    public PaymentServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public String processPayment(Long orderId, String paymentMethod) {
        log.info("Processing payment for order {} with method {}", orderId, paymentMethod);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));

        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BusinessException("Order is not in PENDING status");
        }

        // Simulate payment processing
        // In production, integrate with payment gateway (Stripe, PayPal, etc.)
        boolean paymentSuccess = simulatePaymentGateway(order, paymentMethod);

        if (paymentSuccess) {
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);
            log.info("Payment successful for order {}", orderId);
            return "Payment successful. Order confirmed.";
        } else {
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.save(order);
            log.error("Payment failed for order {}", orderId);
            throw new BusinessException("Payment failed. Please try again.");
        }
    }

    @Override
    public boolean verifyPayment(Long orderId) {
        log.debug("Verifying payment for order {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));

        // In production, verify with payment gateway
        return order.getStatus() == Order.OrderStatus.CONFIRMED;
    }

    @Override
    @Transactional
    public String refundPayment(Long orderId) {
        log.info("Processing refund for order {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));

        if (order.getStatus() != Order.OrderStatus.CONFIRMED &&
                order.getStatus() != Order.OrderStatus.SHIPPED) {
            throw new BusinessException("Order cannot be refunded in current status");
        }

        // Simulate refund processing
        // In production, process refund through payment gateway
        boolean refundSuccess = simulateRefund(order);

        if (refundSuccess) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.save(order);
            log.info("Refund successful for order {}", orderId);
            return "Refund processed successfully.";
        } else {
            log.error("Refund failed for order {}", orderId);
            throw new BusinessException("Refund failed. Please contact support.");
        }
    }

    /**
     * Simulate payment gateway processing
     * In production, replace with actual payment gateway integration
     */
    private boolean simulatePaymentGateway(Order order, String paymentMethod) {
        // Simulate 95% success rate
        log.debug("Simulating payment gateway for order {} with total amount {}",
                order.getId(), order.getTotalAmount());
        return Math.random() > 0.05;
    }

    /**
     * Simulate refund processing
     * In production, replace with actual payment gateway refund
     */
    private boolean simulateRefund(Order order) {
        log.debug("Simulating refund for order {} with amount {}",
                order.getId(), order.getTotalAmount());
        return true; // Always successful in simulation
    }
}
