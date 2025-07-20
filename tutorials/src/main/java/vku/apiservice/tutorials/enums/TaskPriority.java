package vku.apiservice.tutorials.enums;

/**
 * Enum representing the possible priorities of a task
 */
public enum TaskPriority {
  LOW("Low"), MEDIUM("Medium"), HIGH("High"), URGENT("Urgent");

  private final String displayName;

  TaskPriority(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }

  /**
   * Convert string to TaskPriority enum (case-insensitive)
   */
  public static TaskPriority fromString(String priority) {
    if (priority == null) {
      return null;
    }

    for (TaskPriority taskPriority : TaskPriority.values()) {
      if (taskPriority.name().equalsIgnoreCase(priority.trim()) ||
          taskPriority.displayName.equalsIgnoreCase(priority.trim())) {
        return taskPriority;
      }
    }

    throw new IllegalArgumentException("Invalid task priority: " + priority +
        ". Valid values are: LOW, MEDIUM, HIGH, URGENT");
  }
}
