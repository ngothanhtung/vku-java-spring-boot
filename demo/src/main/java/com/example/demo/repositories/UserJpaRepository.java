package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :email")
    Optional<User> findByEmail(String email);
}
