package com.eticaret.backend.controller;

import com.eticaret.backend.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Payment operations.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<String> processPayment(@PathVariable Long orderId,
            @RequestParam String paymentMethod) {
        log.info("Processing payment for order: {} with method: {}", orderId, paymentMethod);
        String result = paymentService.processPayment(orderId, paymentMethod);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/verify/{orderId}")
    public ResponseEntity<Boolean> verifyPayment(@PathVariable Long orderId) {
        log.debug("Verifying payment for order: {}", orderId);
        boolean verified = paymentService.verifyPayment(orderId);
        return ResponseEntity.ok(verified);
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<String> refundPayment(@PathVariable Long orderId) {
        log.info("Processing refund for order: {}", orderId);
        String result = paymentService.refundPayment(orderId);
        return ResponseEntity.ok(result);
    }
}
