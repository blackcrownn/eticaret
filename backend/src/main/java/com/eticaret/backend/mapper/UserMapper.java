package com.eticaret.backend.mapper;

import com.eticaret.backend.dto.request.RegisterRequest;
import com.eticaret.backend.dto.response.UserResponse;
import com.eticaret.backend.model.User;
import org.mapstruct.*;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Map User entity to UserResponse DTO
     * Password is excluded from response (not in UserResponse)
     */
    UserResponse toResponse(User user);

    /**
     * Map RegisterRequest to User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    User toEntity(RegisterRequest request);
}
