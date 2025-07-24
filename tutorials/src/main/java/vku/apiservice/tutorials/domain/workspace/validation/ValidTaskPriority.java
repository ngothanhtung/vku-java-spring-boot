package vku.apiservice.tutorials.domain.workspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Validation annotation for TaskPriority enum fields
 */
@Documented
@Constraint(validatedBy = ValidTaskPriorityValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskPriority {
  String message() default "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}