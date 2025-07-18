package vku.apiservice.tutorials.services;

import vku.apiservice.tutorials.dtos.CreateUserDto;
import vku.apiservice.tutorials.dtos.RoleDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDto data) {
        User user = new User();
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setPassword(data.getPassword());

        return userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAllUsersWithRoles();

        System.out.println("Fetched users: " + users.size());

        return users.stream().map(user -> {
            List<RoleDto> roles = user.getUserRoles().stream().map(userRole -> new RoleDto(userRole.getRole().getId(), userRole.getRole().getName())).collect(Collectors.toList());
            return new UserDto(user.getId(), user.getName(), user.getEmail(), roles);
        }).collect(Collectors.toList());
    }
}