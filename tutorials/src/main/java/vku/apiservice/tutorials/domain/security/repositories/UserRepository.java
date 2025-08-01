package vku.apiservice.tutorials.domain.security.repositories;

import java.util.List;
import java.util.Optional;

import vku.apiservice.tutorials.domain.security.entities.User;

public interface UserRepository {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailWithRoles(String email);

    Optional<User> findById(String id);

    boolean existsById(String id);

    User save(User user);

    void deleteById(String id);

    List<User> findAllUsersWithRoles();

    List<User> findByIdIn(List<String> ids);

}
