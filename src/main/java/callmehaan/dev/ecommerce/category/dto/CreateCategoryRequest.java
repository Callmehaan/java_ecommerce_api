package callmehaan.dev.ecommerce.category.dto;

import callmehaan.dev.ecommerce.category.constant.CategoryStatus;

import java.util.UUID;

public record CreateCategoryRequest(
        String name,
        String description,
        String slug,
        UUID parentId,
        CategoryStatus active,
        Integer sortOrder
) {
}
