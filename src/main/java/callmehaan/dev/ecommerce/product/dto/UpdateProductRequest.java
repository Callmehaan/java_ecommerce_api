package callmehaan.dev.ecommerce.product.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProductRequest(
        String title,
        String description,
        BigDecimal price,
        int stock
) {
}
