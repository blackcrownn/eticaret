package com.eticaret.backend.service.impl;

import com.eticaret.backend.dto.request.CreateProductRequest;
import com.eticaret.backend.dto.request.UpdateProductRequest;
import com.eticaret.backend.dto.response.ProductResponse;
import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.mapper.ProductMapper;
import com.eticaret.backend.model.Category;
import com.eticaret.backend.model.Product;
import com.eticaret.backend.repository.CategoryRepository;
import com.eticaret.backend.repository.ProductRepository;
import com.eticaret.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService.
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        // Validate SKU uniqueness
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("Product with SKU '" + request.getSku() + "' already exists");
        }

        // Validate category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("Category not found with id: " + request.getCategoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.debug("Fetching product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.debug("Fetching all products");

        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getActiveProducts() {
        log.debug("Fetching active products");

        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        log.debug("Fetching products for category: {}", categoryId);

        // Validate category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new BusinessException("Category not found with id: " + categoryId);
        }

        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + id));

        // Validate SKU uniqueness if changed
        if (request.getSku() != null && !request.getSku().equals(product.getSku())) {
            if (productRepository.existsBySku(request.getSku())) {
                throw new BusinessException("Product with SKU '" + request.getSku() + "' already exists");
            }
        }

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }

        productMapper.updateEntityFromRequest(request, product);
        Product updatedProduct = productRepository.save(product);

        log.info("Product updated successfully with id: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Soft deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + id));

        product.setActive(false);
        productRepository.save(product);

        log.info("Product soft deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public void hardDeleteProduct(Long id) {
        log.info("Hard deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new BusinessException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
        log.info("Product hard deleted successfully with id: {}", id);
    }

    @Override
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }
}
