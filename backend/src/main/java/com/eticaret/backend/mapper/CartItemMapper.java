package com.eticaret.backend.mapper;

import com.eticaret.backend.dto.response.CartItemResponse;
import com.eticaret.backend.model.CartItem;
import org.mapstruct.*;

/**
 * MapStruct mapper for CartItem entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ProductMapper.class })
public interface CartItemMapper {

    /**
     * Map CartItem entity to CartItemResponse DTO
     */
    @Mapping(target = "subtotal", expression = "java(cartItem.getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())))")
    CartItemResponse toResponse(CartItem cartItem);
}
