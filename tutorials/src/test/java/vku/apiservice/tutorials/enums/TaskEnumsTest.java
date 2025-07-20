package vku.apiservice.tutorials.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class TaskEnumsTest {

  @Test
  void testTaskStatusFromString() {
    // Test valid cases
    assertEquals(TaskStatus.TO_DO, TaskStatus.fromString("TO_DO"));
    assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromString("IN_PROGRESS"));
    assertEquals(TaskStatus.DONE, TaskStatus.fromString("DONE"));

    // Test case insensitive
    assertEquals(TaskStatus.TO_DO, TaskStatus.fromString("to_do"));
    assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromString("in_progress"));
    assertEquals(TaskStatus.DONE, TaskStatus.fromString("done"));

    // Test display names
    assertEquals(TaskStatus.TO_DO, TaskStatus.fromString("To Do"));
    assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromString("In Progress"));
    assertEquals(TaskStatus.DONE, TaskStatus.fromString("Done"));

    // Test null
    assertNull(TaskStatus.fromString(null));

    // Test invalid
    assertThrows(IllegalArgumentException.class, () -> TaskStatus.fromString("INVALID"));
  }

  @Test
  void testTaskPriorityFromString() {
    // Test valid cases
    assertEquals(TaskPriority.LOW, TaskPriority.fromString("LOW"));
    assertEquals(TaskPriority.MEDIUM, TaskPriority.fromString("MEDIUM"));
    assertEquals(TaskPriority.HIGH, TaskPriority.fromString("HIGH"));
    assertEquals(TaskPriority.URGENT, TaskPriority.fromString("URGENT"));

    // Test case insensitive
    assertEquals(TaskPriority.LOW, TaskPriority.fromString("low"));
    assertEquals(TaskPriority.MEDIUM, TaskPriority.fromString("medium"));
    assertEquals(TaskPriority.HIGH, TaskPriority.fromString("high"));
    assertEquals(TaskPriority.URGENT, TaskPriority.fromString("urgent"));

    // Test display names
    assertEquals(TaskPriority.LOW, TaskPriority.fromString("Low"));
    assertEquals(TaskPriority.MEDIUM, TaskPriority.fromString("Medium"));
    assertEquals(TaskPriority.HIGH, TaskPriority.fromString("High"));
    assertEquals(TaskPriority.URGENT, TaskPriority.fromString("Urgent"));

    // Test null
    assertNull(TaskPriority.fromString(null));

    // Test invalid
    assertThrows(IllegalArgumentException.class, () -> TaskPriority.fromString("INVALID"));
  }
}
