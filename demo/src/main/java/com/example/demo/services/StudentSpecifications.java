package com.example.demo.services;

import com.example.demo.entities.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {
    public static Specification<Student> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
