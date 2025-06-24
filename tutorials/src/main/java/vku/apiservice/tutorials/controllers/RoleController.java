package vku.apiservice.tutorials.controllers;

import vku.apiservice.tutorials.dtos.CreateRoleDto;
import vku.apiservice.tutorials.entities.Role;
import vku.apiservice.tutorials.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping()
    public Role createRole(@RequestBody @Valid CreateRoleDto data) {
        return roleService.createRequest(data);
    }

    @GetMapping()
    public Iterable<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/{id}/add-users-to-role")
    public ResponseEntity<String> addUsersToRole(@PathVariable String id, @RequestBody List<String> userIds) {

        roleService.addUsersToRole(id, userIds);

        return ResponseEntity.ok("Users added to role successfully!");
    }

    @PostMapping("/{id}/remove-users-from-role")
    public ResponseEntity<String> removeUsersFromRole(@PathVariable String id, @RequestBody List<String> userIds) {

        roleService.removeUsersFromRole(id, userIds);

        return ResponseEntity.ok("Users removed from role successfully!");
    }
}