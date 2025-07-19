package vku.apiservice.tutorials.repositories;

import vku.apiservice.tutorials.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role r")
    List<User> findAllUsersWithRoles();

    @Query("SELECT u FROM User u JOIN FETCH u.userRoles ur JOIN FETCH ur.role WHERE u.id = :userId")
    User findByIdWithRoles(String userId);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find user by email
    User findByEmail(String email);
}