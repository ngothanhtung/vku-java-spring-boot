package vku.apiservice.tutorials.domain.security.repositories;

import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;

import java.util.List;
import java.util.Set;

public interface UserRoleRepository {
   boolean existsById(UserRoleId userRoleId);

   void deleteById(UserRoleId userRoleId);


 void saveAllUserRoles(Iterable<UserRole> userRoles); // Đổi tên để tránh clash
}
