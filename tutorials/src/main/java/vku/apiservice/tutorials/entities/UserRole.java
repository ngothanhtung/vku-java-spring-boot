package vku.apiservice.tutorials.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_roles")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id = new UserRoleId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Maps to embedded id userId
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId") // Maps to embedded id roleId
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true; // Your status column
}
