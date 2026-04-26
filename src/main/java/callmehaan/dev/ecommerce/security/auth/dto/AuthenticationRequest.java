package callmehaan.dev.ecommerce.security.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 30)
        String username,
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {
}
