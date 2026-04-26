package callmehaan.dev.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(min = 4, max = 30)
        String username,
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {
}
