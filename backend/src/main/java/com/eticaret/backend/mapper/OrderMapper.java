package com.eticaret.backend.mapper;

import com.eticaret.backend.dto.response.OrderResponse;
import com.eticaret.backend.model.Order;
import org.mapstruct.*;

/**
 * MapStruct mapper for Order entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface OrderMapper {

    /**
     * Map Order entity to OrderResponse DTO
     */
    OrderResponse toResponse(Order order);
}
