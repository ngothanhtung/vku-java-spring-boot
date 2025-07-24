package vku.apiservice.tutorials.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vku.apiservice.tutorials.enums.TaskPriority;

/**
 * Validator implementation for ValidTaskPriority annotation
 */
public class ValidTaskPriorityValidator implements ConstraintValidator<ValidTaskPriority, String> {

  @Override
  public void initialize(ValidTaskPriority constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return true; // Let @NotBlank handle null/empty validation if needed
    }

    try {
      TaskPriority.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
