package vku.apiservice.tutorials.domain.security.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vku.apiservice.tutorials.domain.common.entities.BaseEntity;
import vku.apiservice.tutorials.domain.workspace.entities.Task;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<Task> tasks;
}