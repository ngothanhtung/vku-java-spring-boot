package vku.apiservice.tutorials.controllers;

import vku.apiservice.tutorials.dtos.CreateUserDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto data) {
        User createdUser = userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping()
    public Iterable<UserDto> getUsers() {
        return userService.getUsers();
    }
}