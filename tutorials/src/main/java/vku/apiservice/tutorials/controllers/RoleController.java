package vku.apiservice.tutorials.controllers;

import vku.apiservice.tutorials.dtos.CreateRoleDto;
import vku.apiservice.tutorials.dtos.UpdateRoleDto;
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
    public Role create(@RequestBody @Valid CreateRoleDto data) {
        return roleService.create(data);
    }

    @PatchMapping("/{id}")
    public Role update(@PathVariable("id") String id, @RequestBody @Valid UpdateRoleDto data) {
        // debug print id and data
        System.out.println("Updating role with ID: " + id);
        System.out.println("Update data: " + data);
        return roleService.update(id, data);
    }

    @GetMapping()
    public Iterable<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/{id}/add-users-to-role")
    public ResponseEntity<String> addUsersToRole(@PathVariable("id") String id, @RequestBody List<String> userIds) {

        roleService.addUsersToRole(id, userIds);

        return ResponseEntity.ok("Users added to role successfully!");
    }

    @PostMapping("/{id}/remove-users-from-role")
    public ResponseEntity<String> removeUsersFromRole(@PathVariable("id") String id,
            @RequestBody List<String> userIds) {

        roleService.removeUsersFromRole(id, userIds);

        return ResponseEntity.ok("Users removed from role successfully!");
    }
}