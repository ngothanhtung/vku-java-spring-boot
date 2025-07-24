package vku.apiservice.tutorials.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vku.apiservice.tutorials.config.PreAuthorizeUtil;
import vku.apiservice.tutorials.dtos.CreateProjectRequestDto;
import vku.apiservice.tutorials.dtos.ProjectWithTasksResponseDto;
import vku.apiservice.tutorials.dtos.UpdateProjectRequestDto;
import vku.apiservice.tutorials.entities.Project;
import vku.apiservice.tutorials.services.ProjectService;

@RestController
@RequestMapping("/api/projects")
@PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER)
public class ProjectController {
  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping
  public ResponseEntity<ProjectWithTasksResponseDto> createProject(@RequestBody @Valid CreateProjectRequestDto data) {
    Project project = projectService.create(data);
    ProjectWithTasksResponseDto projectWithTasksResponseDto = projectService.convertToDto(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(projectWithTasksResponseDto);
  }

  @PutMapping("/{id}")
  public ProjectWithTasksResponseDto updateProject(@PathVariable("id") String id, @RequestBody @Valid UpdateProjectRequestDto data) {
    Project project = projectService.updateProject(id, data);
    return projectService.convertToDto(project);
  }

  @PatchMapping("/{id}")
  public ProjectWithTasksResponseDto patchProject(@PathVariable("id") String id, @RequestBody @Valid UpdateProjectRequestDto data) {
    Project project = projectService.updateProject(id, data);
    return projectService.convertToDto(project);
  }

  @GetMapping
  public List<ProjectWithTasksResponseDto> getProjects(
      @RequestParam(value = "includeTasks", defaultValue = "false") boolean includeTasks) {
    if (includeTasks) {
      return projectService.getProjectsWithTasks();
    }
    return projectService.getProjects();
  }

  @GetMapping("/{id}")
  public ProjectWithTasksResponseDto getProjectById(@PathVariable("id") String id) {
    return projectService.getProjectDtoById(id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteProject(@PathVariable("id") String id) {
    projectService.deleteProject(id);
    return ResponseEntity.ok("Project deleted successfully");
  }
}
