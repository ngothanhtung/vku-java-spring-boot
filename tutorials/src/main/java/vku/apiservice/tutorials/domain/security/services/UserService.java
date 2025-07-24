package vku.apiservice.tutorials.domain.security.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.domain.security.dtos.CreateUserRequestDto;
import vku.apiservice.tutorials.domain.security.dtos.RoleResponseDto;
import vku.apiservice.tutorials.domain.security.dtos.UserResponseDto;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;
import vku.apiservice.tutorials.presentation.exceptions.HttpException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(CreateUserRequestDto data) {
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

    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAllUsersWithRoles();

        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserResponseDto> findByEmailWithRoles(String email) {
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new HttpException("User not found with email: " + email, HttpStatus.NOT_FOUND));
        // return user with roles relationships
        return Optional.of(convertToDto(user));
    }

    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpException("User not found with id: " + id, HttpStatus.NOT_FOUND));
        return convertToDto(user);
    }

    public UserResponseDto updateUser(String id, CreateUserRequestDto data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        user.setName(data.getName());
        user.setEmail(data.getEmail());

        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            user.setPassword(data.getPassword());
        }

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new HttpException("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public boolean isCurrentUser(String userId, String currentUserEmail) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getEmail().equals(currentUserEmail);
    }

    /**
     * Converts User entity to UserResponseDto with proper mapping of all fields
     */
    private UserResponseDto convertToDto(User user) {
        List<RoleResponseDto> roles = user.getUserRoles().stream()
                .map(userRole -> new RoleResponseDto(userRole.getRole().getId(), userRole.getRole().getCode(),
                        userRole.getRole().getName()))
                .collect(Collectors.toList());

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}