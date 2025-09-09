package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "Users")
//@Inheritance(strategy = InheritanceType.JOINED)
//// @DiscriminatorColumn(name = "user_type", discriminatorType =
//// DiscriminatorType.STRING)
//public abstract class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    // Dùng cho InheritanceType.TABLE_PER_CLASS
//    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
//    // "user_sequence")
//    // @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence",
//    // allocationSize = 1)
//    private Long id;
//    private String username;
//    private String password;
//}

@Getter
@Setter
@Entity
@Table(name = "Users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
