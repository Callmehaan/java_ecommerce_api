package callmehaan.dev.ecommerce.address;

import callmehaan.dev.ecommerce.address.dto.AddressDto;
import callmehaan.dev.ecommerce.address.dto.CreateAddressDto;
import callmehaan.dev.ecommerce.address.dto.UpdateAddressDto;
import callmehaan.dev.ecommerce.address.entity.Address;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.user.UserRepository;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createAddress(User user, CreateAddressDto createAddressDto) {
        Address address = Address.builder()
                .name(createAddressDto.name())
                .state(createAddressDto.state())
                .city(createAddressDto.city())
                .street(createAddressDto.street())
                .phoneNumber(createAddressDto.phoneNumber())
                .zipcode(createAddressDto.zipcode())
                .email(createAddressDto.email())
                .user(user)
                .build();

        return addressRepository.save(address);
    }

    public PageResponse<AddressDto> findAllAddressesByUserId(User user, Pageable pageable) {
        Page<AddressDto> addressDtoPage = addressRepository.findAllByUserId(user.getId(), pageable);

        return PageResponse.from(addressDtoPage);
    }

    public Address updateAddress(UUID id, UpdateAddressDto updateAddressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setName(updateAddressDto.name() != null ? updateAddressDto.name() : address.getName());
        address.setCity(updateAddressDto.city() != null ? updateAddressDto.city() : address.getCity());
        address.setState(updateAddressDto.state() != null ? updateAddressDto.state() : address.getState());
        address.setStreet(updateAddressDto.street() != null ? updateAddressDto.street() : address.getStreet());
        address.setZipcode(updateAddressDto.zipcode() != null ? updateAddressDto.zipcode() : address.getZipcode());
        address.setEmail(updateAddressDto.email() != null ? updateAddressDto.email() : address.getEmail());
        address.setPhoneNumber(updateAddressDto.phoneNumber() != null
                ? updateAddressDto.phoneNumber() : address.getPhoneNumber());

        return addressRepository.save(address);
    }
}
