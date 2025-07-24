package vku.apiservice.tutorials.domain.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vku.apiservice.tutorials.domain.workspace.entities.Task;
import vku.apiservice.tutorials.domain.common.entities.BaseEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @Embedded
    @Column(unique = true, nullable = false)
    private String email;

    // Exclude password from JSON serialization
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasks;

}