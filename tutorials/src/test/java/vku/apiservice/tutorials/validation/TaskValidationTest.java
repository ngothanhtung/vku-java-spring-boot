package vku.apiservice.tutorials.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vku.apiservice.tutorials.dtos.CreateTaskDto;

class TaskValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidTaskStatusValues() {
    CreateTaskDto dto = new CreateTaskDto();
    dto.setTitle("Valid Title");
    dto.setDescription("Valid Description");
    dto.setStatus("TO_DO");
    dto.setPriority("HIGH");
    dto.setAssigneeId("user-123");

    Set<ConstraintViolation<CreateTaskDto>> violations = validator.validate(dto);
    assertTrue(violations.isEmpty(), "No violations expected for valid enum values");
  }

  @Test
  void testInvalidTaskStatusValues() {
    CreateTaskDto dto = new CreateTaskDto();
    dto.setTitle("Valid Title");
    dto.setDescription("Valid Description");
    dto.setStatus("INVALID_STATUS");
    dto.setPriority("INVALID_PRIORITY");
    dto.setAssigneeId("user-123");

    Set<ConstraintViolation<CreateTaskDto>> violations = validator.validate(dto);
    assertFalse(violations.isEmpty(), "Violations expected for invalid enum values");

    // Check that we have violations for both status and priority
    long statusViolations = violations.stream()
        .filter(v -> v.getPropertyPath().toString().equals("status"))
        .count();
    long priorityViolations = violations.stream()
        .filter(v -> v.getPropertyPath().toString().equals("priority"))
        .count();

    assertTrue(statusViolations > 0, "Status validation should fail");
    assertTrue(priorityViolations > 0, "Priority validation should fail");
  }

  @Test
  void testCaseInsensitiveValues() {
    CreateTaskDto dto = new CreateTaskDto();
    dto.setTitle("Valid Title");
    dto.setDescription("Valid Description");
    dto.setStatus("in_progress");
    dto.setPriority("low");
    dto.setAssigneeId("user-123");

    Set<ConstraintViolation<CreateTaskDto>> violations = validator.validate(dto);
    assertTrue(violations.isEmpty(), "No violations expected for case-insensitive valid enum values");
  }

  @Test
  void testNullValues() {
    CreateTaskDto dto = new CreateTaskDto();
    dto.setTitle("Valid Title");
    dto.setDescription("Valid Description");
    dto.setStatus(null);
    dto.setPriority(null);
    dto.setAssigneeId("user-123");

    Set<ConstraintViolation<CreateTaskDto>> violations = validator.validate(dto);
    assertTrue(violations.isEmpty(), "No violations expected for null enum values (optional fields)");
  }
}
