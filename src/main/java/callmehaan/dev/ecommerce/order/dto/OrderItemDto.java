package callmehaan.dev.ecommerce.order.dto;

import callmehaan.dev.ecommerce.order.entity.Order;
import callmehaan.dev.ecommerce.order.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderItemDto(
        UUID id,
        int quantity,
        BigDecimal priceAtPurchase
) {
    public static List<OrderItemDto> from(Order order) {
        return order.getOrderItems()
                .stream()
                .map(item -> new OrderItemDto(item.getId(), item.getQuantity(), item.getPriceAtPurchase()))
                .toList();
    }
}
