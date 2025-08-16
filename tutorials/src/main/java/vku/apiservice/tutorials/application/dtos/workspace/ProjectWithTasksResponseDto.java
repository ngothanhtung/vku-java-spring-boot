package vku.apiservice.tutorials.application.dtos.workspace;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectWithTasksResponseDto {
    private String id;
    private String name;
    private String description;

    private List<TaskResponseDto> tasks; // Optional: include tasks in the response

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
