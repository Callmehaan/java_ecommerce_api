package callmehaan.dev.ecommerce.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderItemDto(
        @NotNull
        UUID productId,
        @Min(1)
        int quantity
) {
}
