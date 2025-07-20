package vku.apiservice.tutorials.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vku.apiservice.tutorials.enums.TaskPriority;
import vku.apiservice.tutorials.enums.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private TaskStatus status; // Will be handled in service with default

    private TaskPriority priority; // Will be handled in service with default

    @NotBlank(message = "Id of Assignee is required")
    private String assigneeId;
}