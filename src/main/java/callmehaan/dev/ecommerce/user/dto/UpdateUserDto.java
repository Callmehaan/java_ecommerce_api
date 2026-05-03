package callmehaan.dev.ecommerce.user.dto;

import callmehaan.dev.ecommerce.user.constant.Role;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUserDto(
        @Size(min = 4, max = 30)
        String username,
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,
        List<Role> roles
) {
}
