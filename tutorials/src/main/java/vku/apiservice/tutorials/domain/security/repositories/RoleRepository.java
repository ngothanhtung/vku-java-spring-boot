package vku.apiservice.tutorials.domain.security.repositories;

import vku.apiservice.tutorials.domain.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {


}