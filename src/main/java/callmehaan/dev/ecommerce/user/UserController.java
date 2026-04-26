package callmehaan.dev.ecommerce.user;

import callmehaan.dev.ecommerce.common.BaseController;
import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.user.dto.UpdateUserDto;
import callmehaan.dev.ecommerce.user.dto.UserDto;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<PageResponse<UserDto>>> getAllUsers(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PageResponse<UserDto> userDtoPageResponse = userService.getUsers(pageable);

        return ok(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                userDtoPageResponse
        );
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        return ok(HttpStatus.OK.value(), "User fetched successfully", UserDto.from(user));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        return ok(HttpStatus.OK.value(), "User fetched successfully", UserDto.from(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDto userDto) {
        User updatedUser = userService.updateUser(id, userDto);

        return ok(HttpStatus.OK.value(), "User updated successfully", UserDto.from(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "User deleted successfully", null)
        );
    }
}
