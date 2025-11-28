package com.eticaret.backend.service.impl;

import com.eticaret.backend.dto.request.CreateCategoryRequest;
import com.eticaret.backend.dto.request.UpdateCategoryRequest;
import com.eticaret.backend.dto.response.CategoryResponse;
import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.exception.NotFoundException;
import com.eticaret.backend.mapper.CategoryMapper;
import com.eticaret.backend.model.Category;
import com.eticaret.backend.repository.CategoryRepository;
import com.eticaret.backend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CategoryService interface.
 * Contains business logic for category operations.
 */
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating new category with name: {}", request.getName());

        // Check if category with same name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully with id: {}", savedCategory.getId());
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.debug("Fetching category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category", id));

        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.debug("Fetching all categories");

        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {
        log.debug("Fetching active categories");

        return categoryRepository.findByActiveTrue().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        log.info("Updating category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category", id));

        // If name is being updated, check for duplicates
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new BusinessException("Category with name '" + request.getName() + "' already exists");
            }
        }

        categoryMapper.updateEntityFromRequest(request, category);
        Category updatedCategory = categoryRepository.save(category);

        log.info("Category updated successfully with id: {}", id);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Soft deleting category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category", id));

        category.setActive(false);
        categoryRepository.save(category);

        log.info("Category soft deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public void hardDeleteCategory(Long id) {
        log.info("Hard deleting category with id: {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category", id);
        }

        categoryRepository.deleteById(id);
        log.info("Category hard deleted successfully with id: {}", id);
    }
}
