package vku.apiservice.tutorials.domain.workspace.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vku.apiservice.tutorials.domain.workspace.validation.ValidTaskPriority;
import vku.apiservice.tutorials.domain.workspace.validation.ValidTaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @ValidTaskStatus()
    private String status; // Optional, will be handled in service with default

    @ValidTaskPriority()
    private String priority; // Optional, will be handled in service with default

    @NotBlank(message = "Id of Assignee is required")
    private String assigneeId;

    // Optional: Project ID (can be null)
    private String projectId;
}