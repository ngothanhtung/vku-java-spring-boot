package vku.apiservice.tutorials.domain.security.repositories;

import java.util.List;
import java.util.Optional;

import vku.apiservice.tutorials.domain.security.entities.Role;

public interface RoleRepository {
    Role save(Role role);

    Role findByName(String name);

    boolean existsByName(String name);

    Optional<Role> findById(String name);

    List<Role> findAll();
}
