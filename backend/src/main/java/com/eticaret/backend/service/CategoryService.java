package com.eticaret.backend.service;

import com.eticaret.backend.dto.request.CreateCategoryRequest;
import com.eticaret.backend.dto.request.UpdateCategoryRequest;
import com.eticaret.backend.dto.response.CategoryResponse;

import java.util.List;

/**
 * Service interface for Category operations.
 * Defines business logic methods for category management.
 */
public interface CategoryService {

    /**
     * Create a new category
     */
    CategoryResponse createCategory(CreateCategoryRequest request);

    /**
     * Get category by id
     */
    CategoryResponse getCategoryById(Long id);

    /**
     * Get all categories
     */
    List<CategoryResponse> getAllCategories();

    /**
     * Get all active categories
     */
    List<CategoryResponse> getActiveCategories();

    /**
     * Update category
     */
    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

    /**
     * Delete category (soft delete - set active to false)
     */
    void deleteCategory(Long id);

    /**
     * Hard delete category
     */
    void hardDeleteCategory(Long id);
}
