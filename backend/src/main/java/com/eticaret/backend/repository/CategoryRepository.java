package com.eticaret.backend.repository;

import com.eticaret.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity.
 * Provides CRUD operations and custom queries.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by name
     */
    Optional<Category> findByName(String name);

    /**
     * Find all active categories
     */
    List<Category> findByActiveTrue();

    /**
     * Check if category exists by name
     */
    boolean existsByName(String name);
}
