package callmehaan.dev.ecommerce.security.auth;

import callmehaan.dev.ecommerce.security.auth.dto.AuthenticationRequest;
import callmehaan.dev.ecommerce.security.auth.dto.AuthenticationResponse;
import callmehaan.dev.ecommerce.security.jwt.JWTUtil;
import callmehaan.dev.ecommerce.user.UserService;
import callmehaan.dev.ecommerce.security.auth.dto.RegisterUserRequest;
import callmehaan.dev.ecommerce.security.auth.dto.RegisterUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService, JWTUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        userService.addUser(registerUserRequest);
        String token =  jwtUtil.issueToken(registerUserRequest.username(), "ROLE_USER");

        return ResponseEntity.ok(new RegisterUserResponse(token));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest AuthRequest) {
        AuthenticationResponse response = authenticationService.login(AuthRequest);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }
}
