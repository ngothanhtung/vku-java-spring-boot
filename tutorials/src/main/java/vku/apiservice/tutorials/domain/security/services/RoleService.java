package vku.apiservice.tutorials.domain.security.services;

import vku.apiservice.tutorials.domain.security.dtos.CreateRoleRequestDto;
import vku.apiservice.tutorials.domain.security.dtos.UpdateRoleRequestDto;
import vku.apiservice.tutorials.domain.security.entities.Role;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;
import vku.apiservice.tutorials.presentation.exceptions.HttpException;
import vku.apiservice.tutorials.domain.security.repositories.RoleRepository;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;
import vku.apiservice.tutorials.domain.security.repositories.UserRoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository,
            UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public Role create(CreateRoleRequestDto data) {
        Role role = new Role();
        role.setCode(data.getCode());
        role.setName(data.getName());
        role.setDescription(data.getDescription());

        return this.roleRepository.save(role);
    }

    public Role update(String id, UpdateRoleRequestDto role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new HttpException("Role not found with id: " + id, HttpStatus.NOT_FOUND));

        // Validate that at least one field is provided
        if (!role.hasAnyField()) {
            throw new HttpException("At least one field must be provided for update", HttpStatus.BAD_REQUEST);
        }

        // Only update fields that are present in the request
        if (role.getCode() != null) {
            existingRole.setCode(role.getCode());
        }
        if (role.getName() != null) {
            existingRole.setName(role.getName());
        }
        if (role.getDescription() != null) {
            existingRole.setDescription(role.getDescription());
        }

        return this.roleRepository.save(existingRole);
    }

    public Role findById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new HttpException("Role not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void addUsersToRole(String roleId, List<String> userIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new HttpException("Role not found with id: " + roleId, HttpStatus.BAD_REQUEST));

        // 2. Check if all userIds exist
        List<User> users = userRepository.findAllById(userIds);
        Set<String> foundUserIds = users.stream().map(User::getId).collect(Collectors.toSet());

        for (String userId : userIds) {
            if (!foundUserIds.contains(userId)) {
                throw new HttpException("User not found with id: " + userId, HttpStatus.BAD_REQUEST);
            }
        }

        Set<UserRole> userRoles = new HashSet<>();

        for (User user : users) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRole.setEnabled(true); // You can control status here
            userRoles.add(userRole);
        }

        try {
            userRoleRepository.saveAll(userRoles);
        } catch (DataIntegrityViolationException e) {
            throw new HttpException("Database constraint violation: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (HttpException e) {
            throw new HttpException("Failed to assign users to role: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void removeUsersFromRole(String roleId, List<String> userIds) {
        // Check if a role exists
        // Role role = roleRepository.findById(roleId).orElseThrow(() -> new
        // RuntimeException("Role not found with id: " + roleId));
        Role role = roleRepository.findById(roleId).orElse(null);

        if (role == null) {
            throw new RuntimeException("Role not found with id: " + roleId);
        }

        // 2. Check if all userIds exist
        List<User> users = userRepository.findAllById(userIds);
        Set<String> foundUserIds = users.stream().map(User::getId).collect(Collectors.toSet());

        for (String userId : userIds) {
            if (!foundUserIds.contains(userId)) {
                throw new RuntimeException("User not found with id: " + userId);
            }
        }

        // 3. Check the User-Role link exists in users_roles
        for (String userId : userIds) {
            UserRoleId userRoleId = new UserRoleId(userId, roleId);
            boolean exists = userRoleRepository.existsById(userRoleId);

            if (!exists) {
                throw new RuntimeException("User id " + userId + " is not assigned to Role id " + roleId);
            }
        }

        // 4. Now Safe: delete the UserRole links
        for (String userId : userIds) {
            UserRoleId userRoleId = new UserRoleId(userId, roleId);
            userRoleRepository.deleteById(userRoleId);
        }
    }
}