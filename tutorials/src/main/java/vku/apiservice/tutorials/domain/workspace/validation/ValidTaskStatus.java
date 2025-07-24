package vku.apiservice.tutorials.domain.workspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Validation annotation for TaskStatus enum fields
 */
@Documented
@Constraint(validatedBy = ValidTaskStatusValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskStatus {
  String message() default "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}