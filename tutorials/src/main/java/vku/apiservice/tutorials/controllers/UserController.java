package vku.apiservice.tutorials.controllers;

import vku.apiservice.tutorials.config.PreAuthorizeUtil;
import vku.apiservice.tutorials.dtos.CreateUserDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize(PreAuthorizeUtil.ADMIN_ONLY)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid CreateUserDto data) {
        return userService.createUser(data);
    }

    @GetMapping
    public Iterable<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") String id, @RequestBody @Valid CreateUserDto data) {
        return userService.updateUser(id, data);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }
}