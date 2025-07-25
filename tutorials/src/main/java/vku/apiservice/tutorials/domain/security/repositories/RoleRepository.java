package vku.apiservice.tutorials.domain.security.repositories;

import vku.apiservice.tutorials.domain.security.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Role findByName(String name);
    boolean existsByName(String name);
    Optional<Role> findById(String name);

    List<Role> findAll();
}
