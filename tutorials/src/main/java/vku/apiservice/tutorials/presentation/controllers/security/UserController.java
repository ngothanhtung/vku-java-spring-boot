package vku.apiservice.tutorials.presentation.controllers.security;

import vku.apiservice.tutorials.infrastructure.config.PreAuthorizeUtil;
import vku.apiservice.tutorials.domain.security.dtos.CreateUserRequestDto;
import vku.apiservice.tutorials.domain.security.dtos.UserResponseDto;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security/users")
@PreAuthorize(PreAuthorizeUtil.ADMIN_ONLY)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid CreateUserRequestDto data) {
        return userService.createUser(data);
    }

    @GetMapping
    public Iterable<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable("id") String id, @RequestBody @Valid CreateUserRequestDto data) {
        return userService.updateUser(id, data);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }
}