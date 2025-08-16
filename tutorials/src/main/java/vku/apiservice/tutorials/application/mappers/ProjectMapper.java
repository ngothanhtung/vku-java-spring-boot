package vku.apiservice.tutorials.application.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vku.apiservice.tutorials.application.dtos.workspace.ProjectResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.ProjectWithTasksResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.TaskResponseDto;
import vku.apiservice.tutorials.domain.workspace.entities.Project;

@Component
public class ProjectMapper {
    
    private final TaskMapper taskMapper;
    
    public ProjectMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    /**
     * Maps Project entity to simple ProjectResponseDto (without tasks)
     */
    public ProjectResponseDto toProjectResponseDto(Project project) {
        if (project == null) {
            return null;
        }
        
        return new ProjectResponseDto(
            project.getId(),
            project.getName(),
            project.getDescription()
        );
    }

    /**
     * Maps Project entity to ProjectWithTasksResponseDto (without tasks included)
     */
    public ProjectWithTasksResponseDto toProjectWithTasksResponseDto(Project project) {
        if (project == null) {
            return null;
        }
        
        ProjectWithTasksResponseDto dto = new ProjectWithTasksResponseDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        dto.setCreatedBy(project.getCreatedBy());
        dto.setUpdatedBy(project.getUpdatedBy());
        
        // Initialize empty tasks list
        dto.setTasks(Collections.emptyList());
        
        return dto;
    }

    /**
     * Maps Project entity to ProjectWithTasksResponseDto with tasks included
     */
    public ProjectWithTasksResponseDto toProjectWithTasksResponseDtoIncludeTasks(Project project) {
        if (project == null) {
            return null;
        }
        
        ProjectWithTasksResponseDto dto = toProjectWithTasksResponseDto(project);
        
        // Map tasks if they exist
        if (project.getTasks() != null && !project.getTasks().isEmpty()) {
            List<TaskResponseDto> taskDtos = project.getTasks().stream()
                    .map(taskMapper::toTaskResponseDto)
                    .collect(Collectors.toList());
            dto.setTasks(taskDtos);
        }
        
        return dto;
    }

    /**
     * Maps a list of Project entities to ProjectWithTasksResponseDto list (without tasks)
     */
    public List<ProjectWithTasksResponseDto> toProjectWithTasksResponseDtoList(List<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return Collections.emptyList();
        }
        
        return projects.stream()
                .map(this::toProjectWithTasksResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Project entities to ProjectWithTasksResponseDto list (with tasks included)
     */
    public List<ProjectWithTasksResponseDto> toProjectWithTasksResponseDtoListIncludeTasks(List<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return Collections.emptyList();
        }
        
        return projects.stream()
                .map(this::toProjectWithTasksResponseDtoIncludeTasks)
                .collect(Collectors.toList());
    }
}
