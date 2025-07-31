package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
// @DiscriminatorColumn(name = "user_type", discriminatorType =
// DiscriminatorType.STRING)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Dùng cho InheritanceType.TABLE_PER_CLASS
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    // @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    private Long id;
    private String username;
    private String password;
}
