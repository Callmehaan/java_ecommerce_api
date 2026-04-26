package callmehaan.dev.ecommerce.user.dto;

import callmehaan.dev.ecommerce.user.constant.Role;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDto(
        UUID id,
        String username,
        String email,
        List<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
