package callmehaan.dev.ecommerce.product.dto;

import callmehaan.dev.ecommerce.category.entity.Category;
import callmehaan.dev.ecommerce.product.entity.Product;
import callmehaan.dev.ecommerce.storage.entity.Image;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        int stock,
        List<String> imageUrls,
        List<String> categories
) {
    public static ProductDto from(Product product) {
        List<String> imageUrls = product.getImages().stream().map(Image::getUrl).toList();
        List<String> categories = product.getCategories().stream().map(Category::getName).toList();

        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                imageUrls,
                categories
        );
    }
}
