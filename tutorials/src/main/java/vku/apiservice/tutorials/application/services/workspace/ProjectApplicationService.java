package vku.apiservice.tutorials.application.services.workspace;

import java.util.List;

import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.application.dtos.workspace.CreateProjectRequestDto;
import vku.apiservice.tutorials.application.dtos.workspace.ProjectResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.ProjectWithTasksResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.UpdateProjectRequestDto;
import vku.apiservice.tutorials.application.mappers.ProjectMapper;
import vku.apiservice.tutorials.domain.workspace.entities.Project;
import vku.apiservice.tutorials.domain.workspace.services.ProjectService;

@Service
public class ProjectApplicationService {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    public ProjectApplicationService(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    public ProjectResponseDto createProject(CreateProjectRequestDto data) {
        Project createdProject = projectService.create(data.getName(), data.getDescription());
        return projectMapper.toProjectResponseDto(createdProject);
    }

    public List<ProjectWithTasksResponseDto> getProjects() {
        List<Project> projects = projectService.getAllProjects();
        return projectMapper.toProjectWithTasksResponseDtoList(projects);
    }

    public List<ProjectWithTasksResponseDto> getProjectsWithTasks() {
        List<Project> projects = projectService.getAllProjectsWithTasks();
        return projectMapper.toProjectWithTasksResponseDtoListIncludeTasks(projects);
    }

    public ProjectWithTasksResponseDto getProjectById(String id) {
        Project project = projectService.getProjectById(id);
        return projectMapper.toProjectWithTasksResponseDto(project);
    }

    public ProjectWithTasksResponseDto updateProject(String id, UpdateProjectRequestDto data) {
        Project updatedProject = projectService.updateProject(id, data.getName(), data.getDescription());
        return projectMapper.toProjectWithTasksResponseDto(updatedProject);
    }

    public void deleteProject(String id) {
        projectService.deleteProject(id);
    }
}