package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.dtos.CreateUserDto;
import vku.apiservice.tutorials.dtos.RoleDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(CreateUserDto data) {
        // Check if email already exists
        if (userRepository.existsByEmail(data.getEmail())) {
            throw new HttpException("Email already exists: " + data.getEmail(), HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setPassword(data.getPassword()); // Store password as plain text

        return userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAllUsersWithRoles();

        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserDto> findByEmailWithRoles(String email) {
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new HttpException("User not found with email: " + email, HttpStatus.NOT_FOUND));
        // return user with roles relationships
        return Optional.of(convertToDto(user));
    }

    /**
     * Converts User entity to UserDto with proper mapping of all fields
     */
    private UserDto convertToDto(User user) {
        List<RoleDto> roles = user.getUserRoles().stream()
                .map(userRole -> new RoleDto(userRole.getRole().getId(), userRole.getRole().getName()))
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}