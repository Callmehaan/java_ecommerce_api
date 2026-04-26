package callmehaan.dev.ecommerce.address.dto;

import callmehaan.dev.ecommerce.address.entity.Address;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AddressDto(
        String name,
        String city,
        String state,
        String street,
        String zipcode,
        String email,
        @JsonProperty("phone_number") String phoneNumber,
        UUID userId
) {
    public static AddressDto from(Address address) {
        return new AddressDto(
                address.getName(),
                address.getCity(),
                address.getState(),
                address.getStreet(),
                address.getZipcode(),
                address.getEmail(),
                address.getPhoneNumber(),
                address.getUser().getId()
        );
    }
}
