package vku.apiservice.tutorials.application.dtos.workspace;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import vku.apiservice.tutorials.domain.workspace.enums.TaskPriority;
import vku.apiservice.tutorials.domain.workspace.enums.TaskStatus;

@Getter
@Setter
public class TaskResponseDto {
    private String id;
    private String title;
    private String description;

    private Date startDate;
    private Date dueDate;

    private Date completedDate;

    private TaskStatus status;
    private TaskPriority priority;

    private AssigneeResponseDto assignee;

    private ProjectResponseDto project;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}