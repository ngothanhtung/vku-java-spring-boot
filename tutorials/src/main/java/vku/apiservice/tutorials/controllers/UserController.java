package vku.apiservice.tutorials.controllers;

import vku.apiservice.tutorials.dtos.CreateUserDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATORS') or hasAuthority('ADMINISTRATORS')")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto data) {
        User createdUser = userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMINISTRATORS', 'MANAGERS') or hasAnyAuthority('ADMINISTRATORS', 'MANAGERS')")
    public Iterable<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATORS', 'MANAGERS') or hasAnyAuthority('ADMINISTRATORS', 'MANAGERS') or @userService.isCurrentUser(#id, authentication.name)")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATORS', 'MANAGERS') or hasAnyAuthority('ADMINISTRATORS', 'MANAGERS') or @userService.isCurrentUser(#id, authentication.name)")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @RequestBody @Valid CreateUserDto data) {
        UserDto updatedUser = userService.updateUser(id, data);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATORS') or hasAuthority('ADMINISTRATORS')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}