package callmehaan.dev.ecommerce.product.dto;

import callmehaan.dev.ecommerce.product.entity.Product;

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
        List<String> imageUrls
) {
    public static ProductDto from(Product product) {
        ArrayList<String> imageUrls = new ArrayList<>();
        product.getImages().forEach(image -> imageUrls.add(image.getUrl()));
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                imageUrls
        );
    }
}
