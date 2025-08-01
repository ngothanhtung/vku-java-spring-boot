package vku.apiservice.tutorials.infrastructure.persistence.jpa.security;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * Saves all UserRole entities.
     *
     * @param userRoles the iterable of UserRole entities to be saved
     */
    @Override
    @Transactional
    public void saveAllUsersRoles(Iterable<UserRole> userRoles) {
        userRoleSpringDataJpaRepository.saveAll(userRoles);
    }

    /**
     * Checks if a UserRole exists by its ID.
     *
     * @param userRoleId the UserRoleId to check
     * @return true if exists, false otherwise
     */
    @Override
    public boolean existsById(UserRoleId userRoleId) {
        return userRoleSpringDataJpaRepository.existsById(userRoleId);
    }

    /**
     * Deletes a UserRole by its ID.
     *
     * @param userRoleId the UserRoleId to delete
     */
    @Override
    @Transactional
    public void deleteById(UserRoleId userRoleId) {
        userRoleSpringDataJpaRepository.deleteById(userRoleId);
    }

    /**
     * Saves a UserRole entity.
     *
     * @param userRole the UserRole to save
     * @return the saved UserRole
     */
    @Override
    @Transactional
    public UserRole save(UserRole userRole) {
        return userRoleSpringDataJpaRepository.save(userRole);
    }
}
