package vku.apiservice.tutorials.repositories;

import vku.apiservice.tutorials.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {


}