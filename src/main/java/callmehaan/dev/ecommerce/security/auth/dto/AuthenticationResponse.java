package callmehaan.dev.ecommerce.security.auth.dto;

import callmehaan.dev.ecommerce.user.dto.UserDto;

public record AuthenticationResponse(
        String token,
        UserDto userDto
) {
}
