package vku.apiservice.tutorials.infrastructure.persistence.jpa.security;

import org.springframework.data.jpa.repository.JpaRepository;

import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;

public interface UserRoleSpringDataJpaRepository extends JpaRepository<UserRole, UserRoleId> {


}