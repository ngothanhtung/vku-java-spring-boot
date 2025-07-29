package vku.apiservice.tutorials.domain.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId") // Maps to embedded id userId
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId") // Maps to embedded id roleId
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true; // Your status column
}
