package vku.apiservice.tutorials.infrastructure.persistence.jpa.security;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.entities.UserRoleId;
import vku.apiservice.tutorials.domain.security.repositories.UserRoleRepository;

@Repository
@Primary
public class UserRoleJpaRepository implements UserRoleRepository {
    private final UserRoleSpringDataJpaRepository userRoleSpringDataJpaRepository;

    public UserRoleJpaRepository(UserRoleSpringDataJpaRepository userRoleSpringDataJpaRepository) {
        this.userRoleSpringDataJpaRepository = userRoleSpringDataJpaRepository;
    }

    @Override
    public void saveAllUsersRoles(Iterable<UserRole> userRoles) {
        this.userRoleSpringDataJpaRepository.saveAll(userRoles);
    }

    @Override
    public boolean existsById(UserRoleId userRoleId) {
        return this.userRoleSpringDataJpaRepository.existsById(userRoleId);
    }

    @Override
    public void deleteById(UserRoleId userRoleId) {
        this.userRoleSpringDataJpaRepository.deleteById(userRoleId);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return this.userRoleSpringDataJpaRepository.save(userRole);
    }
}
