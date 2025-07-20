package vku.apiservice.tutorials.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vku.apiservice.tutorials.enums.TaskStatus;

/**
 * Validator implementation for ValidTaskStatus annotation
 */
public class ValidTaskStatusValidator implements ConstraintValidator<ValidTaskStatus, String> {

  @Override
  public void initialize(ValidTaskStatus constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return true; // Let @NotBlank handle null/empty validation if needed
    }

    try {
      TaskStatus.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
