package vku.apiservice.tutorials.dtos;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    private String id;
    private String title;
    private String description;

    private Date startDate;
    private Date dueDate;

    private Date completedDate;

    private String status;
    private String priority;

    private AssigneeDto assignee;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}