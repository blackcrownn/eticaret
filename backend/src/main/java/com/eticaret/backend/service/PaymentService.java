package com.eticaret.backend.service;

/**
 * Service interface for Payment operations.
 */
public interface PaymentService {

    /**
     * Process payment for an order
     * 
     * @param orderId       Order ID
     * @param paymentMethod Payment method (CREDIT_CARD, DEBIT_CARD, PAYPAL, etc.)
     * @return Payment result message
     */
    String processPayment(Long orderId, String paymentMethod);

    /**
     * Verify payment status
     * 
     * @param orderId Order ID
     * @return Payment status
     */
    boolean verifyPayment(Long orderId);

    /**
     * Refund payment
     * 
     * @param orderId Order ID
     * @return Refund result message
     */
    String refundPayment(Long orderId);
}
