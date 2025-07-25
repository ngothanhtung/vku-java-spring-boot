package vn.vku.crud.entities;

// Entity class for Category

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id // Primary key for the category
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
