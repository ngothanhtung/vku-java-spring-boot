package vku.apiservice.tutorials.dtos;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import vku.apiservice.tutorials.enums.TaskPriority;
import vku.apiservice.tutorials.enums.TaskStatus;

@Getter
@Setter
public class TaskDto {
    private String id;
    private String title;
    private String description;

    private Date startDate;
    private Date dueDate;

    private Date completedDate;

    private TaskStatus status;
    private TaskPriority priority;

    private AssigneeDto assignee;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}