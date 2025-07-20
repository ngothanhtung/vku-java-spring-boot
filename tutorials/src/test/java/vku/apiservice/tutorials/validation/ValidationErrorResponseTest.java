package vku.apiservice.tutorials.validation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import vku.apiservice.tutorials.dtos.CreateTaskDto;

class ValidationErrorResponseTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testMultipleValidationErrors() {
    CreateTaskDto dto = new CreateTaskDto();
    dto.setTitle(""); // Invalid - empty
    dto.setDescription(""); // Invalid - empty
    dto.setStatus("INVALID_STATUS"); // Invalid enum value
    dto.setPriority("INVALID_PRIORITY"); // Invalid enum value
    dto.setAssigneeId(""); // Invalid - empty

    Set<ConstraintViolation<CreateTaskDto>> violations = validator.validate(dto);
    assertFalse(violations.isEmpty(), "Should have validation violations");
    assertEquals(5, violations.size(), "Should have 5 validation errors");

    // Check that we have all expected error messages
    boolean hasTitleError = violations.stream()
        .anyMatch(v -> v.getMessage().equals("Title is required"));
    boolean hasDescriptionError = violations.stream()
        .anyMatch(v -> v.getMessage().equals("Description is required"));
    boolean hasStatusError = violations.stream()
        .anyMatch(v -> v.getMessage().equals("Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE"));
    boolean hasPriorityError = violations.stream()
        .anyMatch(v -> v.getMessage().equals("Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT"));
    boolean hasAssigneeError = violations.stream()
        .anyMatch(v -> v.getMessage().equals("Id of Assignee is required"));

    assertTrue(hasTitleError, "Should have title validation error");
    assertTrue(hasDescriptionError, "Should have description validation error");
    assertTrue(hasStatusError, "Should have status validation error");
    assertTrue(hasPriorityError, "Should have priority validation error");
    assertTrue(hasAssigneeError, "Should have assignee validation error");
  }

  @Test
  void testValidTaskDto() {
    CreateTaskDto dto = new CreateTaskDto();
    dto.setTitle("Valid Title");
    dto.setDescription("Valid Description");
    dto.setStatus("TO_DO"); // Valid enum value
    dto.setPriority("HIGH"); // Valid enum value
    dto.setAssigneeId("user-123");

    Set<ConstraintViolation<CreateTaskDto>> violations = validator.validate(dto);
    assertTrue(violations.isEmpty(), "Valid DTO should have no violations");
  }
}
