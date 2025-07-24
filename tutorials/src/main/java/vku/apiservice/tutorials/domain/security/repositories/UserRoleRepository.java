package vku.apiservice.tutorials.domain.security.repositories;

import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}