package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// Dùng cho InheritanceType.TABLE_PER_CLASS hoặc JOINED
@Entity
@Table(name = "employees")
public class Employee extends AuthUser {

    private String name;
    private String address;
}
