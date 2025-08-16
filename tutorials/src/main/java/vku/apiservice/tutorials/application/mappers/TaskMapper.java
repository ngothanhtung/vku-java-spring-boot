
package vku.apiservice.tutorials.application.mappers;

import org.springframework.stereotype.Component;

import vku.apiservice.tutorials.application.dtos.workspace.AssigneeResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.ProjectResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.TaskResponseDto;
import vku.apiservice.tutorials.domain.workspace.entities.Task;

@Component
public class TaskMapper {

    public TaskResponseDto toTaskResponseDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        
        // Basic fields
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        
        // Date fields
        dto.setStartDate(task.getStartDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedDate(task.getCompletedDate());
        
        // Enum fields
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        
        // Assignee mapping
        if (task.getAssignee() != null) {
            dto.setAssignee(AssigneeResponseDto.builder()
                    .id(task.getAssignee().getId())
                    .name(task.getAssignee().getName())
                    .email(task.getAssignee().getEmail())
                    .build());
        }
        
        // Project mapping
        if (task.getProject() != null) {
            dto.setProject(new ProjectResponseDto(
                    task.getProject().getId(),
                    task.getProject().getName(),
                    task.getProject().getDescription()));
        }
        
        // Audit fields
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setCreatedBy(task.getCreatedBy());
        dto.setUpdatedBy(task.getUpdatedBy());
        
        return dto;
    }
}
