package callmehaan.dev.ecommerce.category.dto;

import callmehaan.dev.ecommerce.category.constant.CategoryStatus;

import java.util.UUID;

public record UpdateCategoryRequest(
        String name,
        String description,
        String slug,
        UUID parentId,
        Integer level,
        Integer sortOrder,
        CategoryStatus activate
) {
}
