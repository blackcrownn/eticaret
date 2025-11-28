package com.eticaret.backend.service;

import com.eticaret.backend.dto.request.CreateProductRequest;
import com.eticaret.backend.dto.request.UpdateProductRequest;
import com.eticaret.backend.dto.response.ProductResponse;

import java.util.List;

/**
 * Service interface for Product operations.
 */
public interface ProductService {

    /**
     * Create a new product
     */
    ProductResponse createProduct(CreateProductRequest request);

    /**
     * Get product by id
     */
    ProductResponse getProductById(Long id);

    /**
     * Get all products
     */
    List<ProductResponse> getAllProducts();

    /**
     * Get all active products
     */
    List<ProductResponse> getActiveProducts();

    /**
     * Get products by category
     */
    List<ProductResponse> getProductsByCategory(Long categoryId);

    /**
     * Update product
     */
    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    /**
     * Delete product (soft delete - set active to false)
     */
    void deleteProduct(Long id);

    /**
     * Hard delete product
     */
    void hardDeleteProduct(Long id);

    /**
     * Check if SKU exists
     */
    boolean existsBySku(String sku);
}
