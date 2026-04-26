package callmehaan.dev.ecommerce.address;

import callmehaan.dev.ecommerce.address.dto.AddressDto;
import callmehaan.dev.ecommerce.address.dto.CreateAddressDto;
import callmehaan.dev.ecommerce.address.dto.UpdateAddressDto;
import callmehaan.dev.ecommerce.address.entity.Address;
import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(
            @AuthenticationPrincipal User userDetails,
            @RequestBody CreateAddressDto address) {
        Address createdAddress = addressService.createAddress(userDetails, address);
        ApiResponse<AddressDto> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(), "Address created successfully", AddressDto.from(createdAddress));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<PageResponse<AddressDto>>> findAllAddresses(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PageResponse<AddressDto> addressDtos = addressService.findAllAddressesByUserId(user, pageable);
        ApiResponse<PageResponse<AddressDto>> apiResponse = ApiResponse
                .success(HttpStatus.OK.value(), "Address list found", addressDtos);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(@PathVariable UUID id, UpdateAddressDto address) {
        Address updatedAddress = addressService.updateAddress(id, address);

        ApiResponse<AddressDto> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(), "Address updated successfully", AddressDto.from(updatedAddress));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
