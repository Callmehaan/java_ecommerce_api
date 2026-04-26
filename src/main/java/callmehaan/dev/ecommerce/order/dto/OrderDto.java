package callmehaan.dev.ecommerce.order.dto;

import callmehaan.dev.ecommerce.order.constant.OrderStatus;
import callmehaan.dev.ecommerce.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID id,
        BigDecimal totalPrice,
        OrderStatus status,
        List<OrderItemDto> orderItems,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus(),
                OrderItemDto.from(order),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
