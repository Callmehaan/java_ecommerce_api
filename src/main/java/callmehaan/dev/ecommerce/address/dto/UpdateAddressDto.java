package callmehaan.dev.ecommerce.address.dto;

public record UpdateAddressDto(
        String name,
        String city,
        String state,
        String street,
        String zipcode,
        String email,
        String phoneNumber
) {
}
