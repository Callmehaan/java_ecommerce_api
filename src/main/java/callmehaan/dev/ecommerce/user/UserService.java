package callmehaan.dev.ecommerce.user;

import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.user.constant.Role;
import callmehaan.dev.ecommerce.security.auth.dto.RegisterUserRequest;
import callmehaan.dev.ecommerce.user.dto.UpdateUserDto;
import callmehaan.dev.ecommerce.user.dto.UserDto;
import callmehaan.dev.ecommerce.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addUser(RegisterUserRequest userDto) {
        User user = User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public PageResponse<UserDto> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDto> userDtoPage = users.map(UserDto::from);

        return PageResponse.from(userDtoPage);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(UUID id, UpdateUserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUsername(userDto.username() != null ? userDto.username() : user.getUsername());
        user.setPassword(userDto.password() != null ? passwordEncoder.encode(userDto.password()) : user.getPassword());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
