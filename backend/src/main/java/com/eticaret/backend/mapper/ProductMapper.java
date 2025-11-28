package com.eticaret.backend.mapper;

import com.eticaret.backend.dto.request.CreateProductRequest;
import com.eticaret.backend.dto.request.UpdateProductRequest;
import com.eticaret.backend.dto.response.ProductResponse;
import com.eticaret.backend.model.Product;
import org.mapstruct.*;

/**
 * MapStruct mapper for Product entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { CategoryMapper.class })
public interface ProductMapper {

    /**
     * Map Product entity to ProductResponse DTO
     */
    ProductResponse toResponse(Product product);

    /**
     * Map CreateProductRequest to Product entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "category", ignore = true)
    Product toEntity(CreateProductRequest request);

    /**
     * Update Product entity from UpdateProductRequest
     * Only updates non-null fields
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(UpdateProductRequest request, @MappingTarget Product product);
}
