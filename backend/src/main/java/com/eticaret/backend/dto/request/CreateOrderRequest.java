package com.eticaret.backend.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO for creating an order.
 */
public class CreateOrderRequest {

    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
