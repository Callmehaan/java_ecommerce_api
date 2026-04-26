package callmehaan.dev.ecommerce.address;

import callmehaan.dev.ecommerce.address.dto.AddressDto;
import callmehaan.dev.ecommerce.address.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("""
        select new callmehaan.dev.ecommerce.address.dto.AddressDto(
            a.name,
            a.city,
            a.state,
            a.street,
            a.zipcode,
            a.email,
            a.phoneNumber,
            a.user.id
        )
        from Address a
        where a.user.id = :userId
    """)
    Page<AddressDto> findAllByUserId(UUID userId,  Pageable pageable);
}
