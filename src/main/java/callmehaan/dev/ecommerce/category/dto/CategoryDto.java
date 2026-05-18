package callmehaan.dev.ecommerce.category.dto;

import callmehaan.dev.ecommerce.category.constant.CategoryStatus;
import callmehaan.dev.ecommerce.category.entity.Category;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record CategoryDto(
        UUID id,
        String name,
        String description,
        String slug,
        Integer level,
        CategoryStatus active,
        Integer sortOrder,
        List<CategoryDto> subCategories
) {
    public CategoryDto {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        //? Ensure subCategories is never null
        subCategories = subCategories != null ? List.copyOf(subCategories) : List.of();
    }

    public static CategoryDto fromEntity(Category category) {
        if (category == null) return null;

        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getSlug(),
                category.getLevel(),
                category.getActive(),
                category.getSortOrder(),
                convertSubCategories(category.getSubCategories())
        );
    }

    //? Shallow conversion (without children)
    public static CategoryDto fromEntityShallow(Category category) {
        if (category == null) return null;

        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getSlug(),
                category.getLevel(),
                category.getActive(),
                category.getSortOrder(),
                List.of() // Empty list instead of children
        );
    }

    public static List<CategoryDto> fromEntities(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories.stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    //? Convert list shallow
    public static List<CategoryDto> fromEntitiesShallow(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories.stream()
                .map(CategoryDto::fromEntityShallow)
                .collect(Collectors.toList());
    }

    // Helper method(s)
    private static List<CategoryDto> convertSubCategories(List<Category> subCategories) {
        if (subCategories == null || subCategories.isEmpty()) {
            return List.of();
        }

        return subCategories.stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }
}
