package vku.apiservice.tutorials.domain.security.repositories;

import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;

public interface UserRoleRepository {
    boolean existsById(UserRoleId userRoleId);

    void deleteById(UserRoleId userRoleId);

    void saveAllUsersRoles(Iterable<UserRole> userRoles);

    UserRole save(UserRole userRole);
}
