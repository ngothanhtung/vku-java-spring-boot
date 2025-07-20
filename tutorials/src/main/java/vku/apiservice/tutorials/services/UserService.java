package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.dtos.CreateUserDto;
import vku.apiservice.tutorials.dtos.RoleDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDto data) {
        // Check if email already exists
        if (userRepository.existsByEmail(data.getEmail())) {
            throw new HttpException("Email already exists: " + data.getEmail(), HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setPassword(data.getPassword());

        return userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAllUsersWithRoles();

        return users.stream().map(this::convertToDto).collect(Collectors.toList());
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