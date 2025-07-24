package vku.apiservice.tutorials.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

  @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.tasks")
  List<Project> findAllProjectsWithTasks();

  boolean existsByName(String name);
}
