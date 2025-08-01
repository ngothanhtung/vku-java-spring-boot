package vku.apiservice.tutorials.infrastructure.persistence.jpa.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String>, UserRepository {
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role r")
    @Override
    List<User> findAllUsersWithRoles();

    @Query("SELECT u FROM User u JOIN FETCH u.userRoles ur JOIN FETCH ur.role WHERE u.id = :userId")
    User findByIdWithRoles(String userId);

    // Check if email already exists
    @Override
    boolean existsByEmail(String email);

    // Find the user by email
    @Override
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.email = :email")
    @Override
    Optional<User> findByEmailWithRoles(String email);
}