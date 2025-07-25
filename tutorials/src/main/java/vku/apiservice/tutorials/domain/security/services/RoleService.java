package vku.apiservice.tutorials.domain.security.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vku.apiservice.tutorials.domain.security.dtos.CreateRoleRequestDto;
import vku.apiservice.tutorials.domain.security.dtos.UpdateRoleRequestDto;
import vku.apiservice.tutorials.domain.security.entities.Role;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;
import vku.apiservice.tutorials.domain.security.events.RoleAssignedEvent;
import vku.apiservice.tutorials.domain.security.repositories.RoleRepository;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;
import vku.apiservice.tutorials.domain.security.repositories.UserRoleRepository;
import vku.apiservice.tutorials.presentation.exceptions.HttpException;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;

	private final ApplicationEventPublisher eventPublisher;

	public RoleService(RoleRepository roleRepository,
			UserRepository userRepository,
			UserRoleRepository userRoleRepository,
			ApplicationEventPublisher eventPublisher) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.eventPublisher = eventPublisher;
	}

	public Role create(CreateRoleRequestDto data) {
		Role role = new Role();
		role.setCode(data.getCode());
		role.setName(data.getName());
		role.setDescription(data.getDescription());

		return this.roleRepository.save(role);
	}

	public Role update(String id, UpdateRoleRequestDto role) {
		Role existingRole = this.roleRepository.findById(id)
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
		return this.roleRepository.findById(id)
				.orElseThrow(() -> new HttpException("Role not found with id: " + id, HttpStatus.NOT_FOUND));
	}

	public List<Role> getRoles() {
		return this.roleRepository.findAll();
	}

	// Transactional methods for adding and removing users from roles
	// Only allow adding/removing users if the role exists and all userIds are valid
	// If any userId is invalid, throw an exception
	// If the role does not exist, throw an exception
	// If the user is already assigned to the role, do not add them again
	// If the user is not assigned to the role, do not remove them
	@Transactional
	public void addUsersToRole(String roleId, List<String> userIds) {

		// Check if all userIds exist
		List<User> users = userRepository.findByIdIn(userIds);
		Set<String> foundUserIds = users.stream().map(User::getId).collect(Collectors.toSet());

		for (String userId : userIds) {
			if (!foundUserIds.contains(userId)) {
				throw new HttpException("User not found with id: " + userId, HttpStatus.BAD_REQUEST);
			}
		}

		// Check the User-Role link exists in users_roles
		Role role = this.roleRepository.findById(roleId)
				.orElseThrow(() -> new HttpException("Role not found with id: " + roleId, HttpStatus.NOT_FOUND));
		for (String userId : userIds) {
			UserRoleId userRoleId = new UserRoleId(userId, roleId);
			boolean exists = userRoleRepository.existsById(userRoleId);

			if (exists) {
				// User is already assigned to the role
				continue;
			}

			// If we reach here, the user is not assigned to the role
			UserRole userRole = new UserRole();
			userRole.setId(userRoleId);
			// Set User and Role objects to avoid null one-to-one property error
			User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
			userRole.setUser(user);
			userRole.setRole(role);
			userRoleRepository.save(userRole);

			// Phát sự kiện RoleAssignedEvent
			eventPublisher.publishEvent(new RoleAssignedEvent(user.getId(), roleId));
		}
	}

	public void removeUsersFromRole(String roleId, List<String> userIds) {
		// Check if a role exists
		Role role = this.roleRepository.findById(roleId).orElse(null);

		if (role == null) {
			throw new HttpException("Role not found with id: " + roleId, HttpStatus.BAD_REQUEST);
		}

		// 2. Check if all userIds exist
		List<User> users = userRepository.findByIdIn(userIds);
		Set<String> foundUserIds = users.stream().map(User::getId).collect(Collectors.toSet());

		for (String userId : userIds) {
			if (!foundUserIds.contains(userId)) {
				throw new HttpException("User not found with id: " + userId, HttpStatus.BAD_REQUEST);
			}
		}

		// 4. Now Safe: delete the UserRoleRepository links
		for (String userId : userIds) {
			UserRoleId userRoleId = new UserRoleId(userId, roleId);
			userRoleRepository.deleteById(userRoleId);
		}
	}
}