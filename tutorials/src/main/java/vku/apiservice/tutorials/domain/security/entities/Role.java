package vku.apiservice.tutorials.domain.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vku.apiservice.tutorials.domain.common.entities.BaseEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity()
@Table(name = "roles")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String code;
    private String name;
    private String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<UserRole> userRoles;
}
