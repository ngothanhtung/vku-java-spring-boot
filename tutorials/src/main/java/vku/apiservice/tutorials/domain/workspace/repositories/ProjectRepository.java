package vku.apiservice.tutorials.domain.workspace.repositories;

import vku.apiservice.tutorials.domain.workspace.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findById(String id);
    List<Project> findAllProjectsWithTasks();
    boolean existsByName(String name);
    Project save(Project project);
    void delete(Project project);
    List<Project> findAll();
}