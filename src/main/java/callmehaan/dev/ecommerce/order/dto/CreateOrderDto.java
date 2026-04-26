package callmehaan.dev.ecommerce.order.dto;

import java.util.List;

public record CreateOrderDto(
        List<CreateOrderItemDto> items
) {
}
