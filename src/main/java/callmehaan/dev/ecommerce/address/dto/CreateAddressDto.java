package callmehaan.dev.ecommerce.address.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAddressDto(
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String street,
        @NotBlank String zipcode,
        @Email @NotBlank String email,
        @NotBlank String phoneNumber
) {
}
