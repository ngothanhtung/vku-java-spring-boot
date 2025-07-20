package vku.apiservice.tutorials.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vku.apiservice.tutorials.enums.TaskPriority;
import vku.apiservice.tutorials.enums.TaskStatus;

/**
 * DTO for updating task information.
 * All fields are optional to support partial updates - only non-null fields will be updated.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {

    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;

    @Size(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String assigneeId;

    /**
     * Checks if at least one field is provided for update
     */
    public boolean hasAnyField() {
        return title != null ||
                description != null ||
                status != null ||
                priority != null ||
                assigneeId != null;
    }

    /**
     * Checks if the field has a valid (non-null, non-empty) value
     */
    public boolean hasValidTitle() {
        return title != null && !title.trim().isEmpty();
    }

    public boolean hasValidDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasValidStatus() {
        return status != null;
    }

    public boolean hasValidPriority() {
        return priority != null;
    }

    public boolean hasValidAssigneeId() {
        return assigneeId != null && !assigneeId.trim().isEmpty();
    }
}