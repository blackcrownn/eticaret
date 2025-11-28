package com.eticaret.backend.mapper;

import com.eticaret.backend.dto.request.CreateCategoryRequest;
import com.eticaret.backend.dto.request.UpdateCategoryRequest;
import com.eticaret.backend.dto.response.CategoryResponse;
import com.eticaret.backend.model.Category;
import org.mapstruct.*;

/**
 * MapStruct mapper for Category entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    /**
     * Map Category entity to CategoryResponse DTO
     */
    CategoryResponse toResponse(Category category);

    /**
     * Map CreateCategoryRequest to Category entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    Category toEntity(CreateCategoryRequest request);

    /**
     * Update Category entity from UpdateCategoryRequest
     * Only updates non-null fields
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateCategoryRequest request, @MappingTarget Category category);
}
