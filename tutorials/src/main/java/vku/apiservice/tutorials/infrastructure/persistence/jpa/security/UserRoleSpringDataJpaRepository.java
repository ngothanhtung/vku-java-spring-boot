package vku.apiservice.tutorials.infrastructure.persistence.jpa.security;

import org.springframework.data.jpa.repository.JpaRepository;

import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;
import vku.apiservice.tutorials.domain.security.repositories.UserRoleRepository;

public interface UserRoleJpaRepository extends JpaRepository<UserRole, UserRoleId>, UserRoleRepository {
    @Override
    default void saveAllUserRoles(Iterable<UserRole> userRoles) {
        saveAll(userRoles);
    }
}