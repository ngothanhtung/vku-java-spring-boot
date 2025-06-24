package vku.apiservice.tutorials.repositories;

import vku.apiservice.tutorials.entities.UserRole;
import vku.apiservice.tutorials.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}