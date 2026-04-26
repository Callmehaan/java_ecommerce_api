package callmehaan.dev.ecommerce.order;

import callmehaan.dev.ecommerce.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.product"})
    @Query("""
        select o
        from Order o
        where o.user.id = :userId
    """)
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);
}
