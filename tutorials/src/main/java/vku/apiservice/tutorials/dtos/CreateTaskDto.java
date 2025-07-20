package vku.apiservice.tutorials.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vku.apiservice.tutorials.validation.ValidTaskPriority;
import vku.apiservice.tutorials.validation.ValidTaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @ValidTaskStatus
    private String status; // Optional, will be handled in service with default

    @ValidTaskPriority
    private String priority; // Optional, will be handled in service with default

    @NotBlank(message = "Id of Assignee is required")
    private String assigneeId;
}