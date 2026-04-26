package callmehaan.dev.ecommerce.order;

import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.order.dto.CreateOrderDto;
import callmehaan.dev.ecommerce.order.dto.CreateOrderItemDto;
import callmehaan.dev.ecommerce.order.dto.OrderDto;
import callmehaan.dev.ecommerce.order.entity.Order;
import callmehaan.dev.ecommerce.order.entity.OrderItem;
import callmehaan.dev.ecommerce.product.ProductRepository;
import callmehaan.dev.ecommerce.product.entity.Product;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderDto orderDto, User user) {
        Order order = Order.create(user);

        for (CreateOrderItemDto itemDto : orderDto.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.quantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            order.addItem(orderItem);
        }

        return orderRepository.save(order);
    }

    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    public PageResponse<OrderDto> getAllByUserId(UUID userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        Page<OrderDto> orderDtos = orders.map(OrderDto::from);

        return PageResponse.from(orderDtos);
    }
}
