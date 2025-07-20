package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.dtos.CreateProjectDto;
import vku.apiservice.tutorials.dtos.ProjectDto;
import vku.apiservice.tutorials.dtos.ProjectSummaryDto;
import vku.apiservice.tutorials.dtos.TaskDto;
import vku.apiservice.tutorials.dtos.UpdateProjectDto;
import vku.apiservice.tutorials.entities.Project;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.ProjectRepository;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final TaskService taskService;

  public ProjectService(ProjectRepository projectRepository, TaskService taskService) {
    this.projectRepository = projectRepository;
    this.taskService = taskService;
  }

  public Project create(CreateProjectDto data) {
    // Check if project name already exists
    if (projectRepository.existsByName(data.getName())) {
      throw new HttpException("Project name already exists: " + data.getName(), HttpStatus.CONFLICT);
    }

    Project project = new Project();
    project.setName(data.getName());
    project.setDescription(data.getDescription());

    return this.projectRepository.save(project);
  }

  public List<ProjectDto> getProjects() {
    List<Project> projects = projectRepository.findAll();
    return projects.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public List<ProjectDto> getProjectsWithTasks() {
    List<Project> projects = projectRepository.findAllProjectsWithTasks();
    return projects.stream().map(this::convertToDtoWithTasks).collect(Collectors.toList());
  }

  public Project getProjectById(String id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new HttpException("Project not found with id: " + id, HttpStatus.NOT_FOUND));
  }

  public ProjectDto getProjectDtoById(String id) {
    Project project = getProjectById(id);
    return convertToDto(project);
  }

  public Project updateProject(String id, UpdateProjectDto data) {
    if (!data.hasAnyField()) {
      throw new HttpException("At least one field must be provided for update", HttpStatus.BAD_REQUEST);
    }

    Project project = getProjectById(id);

    if (data.hasValidName()) {
      // Check if the new name already exists (excluding current project)
      if (projectRepository.existsByName(data.getName()) && !project.getName().equals(data.getName())) {
        throw new HttpException("Project name already exists: " + data.getName(), HttpStatus.CONFLICT);
      }
      project.setName(data.getName());
    }

    if (data.hasValidDescription()) {
      project.setDescription(data.getDescription());
    }

    return projectRepository.save(project);
  }

  public void deleteProject(String id) {
    Project project = getProjectById(id);
    // Note: This will set project_id to null for all related tasks due to the
    // nullable foreign key
    projectRepository.delete(project);
  }

  public ProjectDto convertToDto(Project project) {
    ProjectDto dto = new ProjectDto();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setDescription(project.getDescription());

    // Map audit fields
    dto.setCreatedAt(project.getCreatedAt());
    dto.setUpdatedAt(project.getUpdatedAt());
    dto.setCreatedBy(project.getCreatedBy());
    dto.setUpdatedBy(project.getUpdatedBy());

    return dto;
  }

  public ProjectDto convertToDtoWithTasks(Project project) {
    ProjectDto dto = convertToDto(project);

    // Convert tasks to DTOs if they exist
    if (project.getTasks() != null && !project.getTasks().isEmpty()) {
      List<TaskDto> taskDtos = project.getTasks().stream()
          .map(taskService::convertToDto)
          .collect(Collectors.toList());
      dto.setTasks(taskDtos);
    }

    return dto;
  }

  public ProjectSummaryDto convertToSummaryDto(Project project) {
    return new ProjectSummaryDto(
        project.getId(),
        project.getName(),
        project.getDescription());
  }
}
