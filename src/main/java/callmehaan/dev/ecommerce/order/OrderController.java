package callmehaan.dev.ecommerce.order;

import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.order.dto.CreateOrderDto;
import callmehaan.dev.ecommerce.order.dto.OrderDto;
import callmehaan.dev.ecommerce.order.entity.Order;
import callmehaan.dev.ecommerce.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrderDto createOrderDto
    ) {
        Order order = orderService.createOrder(createOrderDto, user);
        ApiResponse<OrderDto> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(), "Order created successfully", OrderDto.from(order));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable UUID id) {
        Order order = orderService.findById(id);
        ApiResponse<OrderDto> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(), "Order found successfully", OrderDto.from(order));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("user_id/{userId}") //TODO Need to fix
    public ResponseEntity<ApiResponse<PageResponse<OrderDto>>> getOrdersByUserId(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable
            UUID userId
    ) {
        PageResponse<OrderDto> pageResponse = orderService.getAllByUserId(userId, pageable);
        ApiResponse<PageResponse<OrderDto>> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(), "Orders fetched successfully", pageResponse);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
