package vku.apiservice.tutorials.domain.workspace.services;

import java.util.List;

import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.domain.common.exceptions.EntityAlreadyExistsException;
import vku.apiservice.tutorials.domain.common.exceptions.EntityNotFoundException;
import vku.apiservice.tutorials.domain.workspace.entities.Project;
import vku.apiservice.tutorials.domain.workspace.repositories.ProjectRepository;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;

  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public Project create(String name, String description) {
    // Domain logic: Check business rule
    if (projectRepository.existsByName(name)) {
      throw new EntityAlreadyExistsException("Project name already exists: " + name);
    }

    // Create domain entity
    Project project = new Project();
    project.setName(name);
    project.setDescription(description);

    return projectRepository.save(project);
  }

  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }

  public List<Project> getAllProjectsWithTasks() {
    return projectRepository.findAllProjectsWithTasks();
  }

  public Project getProjectById(String id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
  }

  public Project updateProject(String id, String name, String description) {
    Project project = getProjectById(id);

    // Domain logic: Validate business rules
    if (name != null && !name.trim().isEmpty()) {
      if (projectRepository.existsByName(name) && !project.getName().equals(name)) {
        throw new EntityAlreadyExistsException("Project name already exists: " + name);
      }
      project.setName(name);
    }

    if (description != null) {
      project.setDescription(description);
    }

    return projectRepository.save(project);
  }

  public void deleteProject(String id) {
    Project project = getProjectById(id);
    projectRepository.delete(project);
  }  
}
