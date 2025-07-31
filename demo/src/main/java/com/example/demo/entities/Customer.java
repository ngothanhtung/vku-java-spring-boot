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
@Table(name = "customers")
public class Customer extends AuthUser {

    private String type;
    private String description;
}
