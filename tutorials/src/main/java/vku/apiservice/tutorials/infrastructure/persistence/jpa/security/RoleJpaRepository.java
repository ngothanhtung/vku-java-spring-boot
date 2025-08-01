package vku.apiservice.tutorials.infrastructure.persistence.jpa.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.domain.security.entities.Role;
import vku.apiservice.tutorials.domain.security.repositories.RoleRepository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, String>, RoleRepository {

}