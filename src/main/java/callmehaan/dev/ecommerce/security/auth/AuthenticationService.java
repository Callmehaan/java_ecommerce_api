package callmehaan.dev.ecommerce.security.auth;

import callmehaan.dev.ecommerce.security.auth.dto.AuthenticationRequest;
import callmehaan.dev.ecommerce.security.auth.dto.AuthenticationResponse;
import callmehaan.dev.ecommerce.security.jwt.JWTUtil;
import callmehaan.dev.ecommerce.user.dto.UserDto;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user = (User) authentication.getPrincipal();
        UserDto userDto = UserDto.from(user);
        String token = jwtUtil.issueToken(userDto.username(), userDto.roles());

        return new AuthenticationResponse(token, userDto);
    }
}
