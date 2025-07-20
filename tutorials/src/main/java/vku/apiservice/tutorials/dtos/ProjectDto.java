package vku.apiservice.tutorials.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {
  private String id;
  private String name;
  private String description;

  private List<TaskDto> tasks; // Optional: include tasks in the response

  // Audit fields
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;
}
