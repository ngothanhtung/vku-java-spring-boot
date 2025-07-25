package vku.apiservice.tutorials.infrastructure.persistence.jpa.workspace;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.domain.workspace.entities.Project;
import vku.apiservice.tutorials.domain.workspace.repositories.ProjectRepository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, String>, ProjectRepository {
  @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.tasks")
  List<Project> findAllProjectsWithTasks();
}
