package com.eticaret.backend.controller;

import com.eticaret.backend.dto.request.CreateCategoryRequest;
import com.eticaret.backend.dto.request.UpdateCategoryRequest;
import com.eticaret.backend.dto.response.ApiResponse;
import com.eticaret.backend.dto.response.CategoryResponse;
import com.eticaret.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category operations.
 * Handles HTTP requests for category management.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Create a new category
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        log.info("REST request to create category: {}", request.getName());

        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", response));
    }

    /**
     * Get category by id
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        log.info("REST request to get category by id: {}", id);

        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all categories
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        log.info("REST request to get all categories, activeOnly: {}", activeOnly);

        List<CategoryResponse> response = activeOnly
                ? categoryService.getActiveCategories()
                : categoryService.getAllCategories();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        log.info("REST request to update category with id: {}", id);

        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", response));
    }

    /**
     * Delete category (soft delete)
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        log.info("REST request to delete category with id: {}", id);

        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
    }

    /**
     * Hard delete category
     * DELETE /api/categories/{id}/hard
     */
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteCategory(@PathVariable Long id) {
        log.info("REST request to hard delete category with id: {}", id);

        categoryService.hardDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category permanently deleted"));
    }
}
