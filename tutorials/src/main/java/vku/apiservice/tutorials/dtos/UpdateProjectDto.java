package vku.apiservice.tutorials.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating project information.
 * All fields are optional to support partial updates - only non-null fields
 * will be updated.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectDto {

  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  private String name;

  @Size(max = 1000, message = "Description cannot exceed 1000 characters")
  private String description;

  /**
   * Checks if at least one field is provided for update
   */
  public boolean hasAnyField() {
    return name != null || description != null;
  }

  /**
   * Checks if the field has a valid (non-null, non-empty) value
   */
  public boolean hasValidName() {
    return name != null && !name.trim().isEmpty();
  }

  public boolean hasValidDescription() {
    return description != null && !description.trim().isEmpty();
  }
}
