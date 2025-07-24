package vku.apiservice.tutorials.enums;

/**
 * Enum representing the possible statuses of a task
 */
public enum TaskStatus {
  TO_DO("To Do"), IN_PROGRESS("In Progress"), DONE("Done");

  private final String displayName;

  TaskStatus(String displayName) {
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
   * Convert string to TaskStatus enum (case-insensitive)
   */
  public static TaskStatus fromString(String status) {
    if (status == null) {
      return null;
    }

    for (TaskStatus taskStatus : TaskStatus.values()) {
      if (taskStatus.name().equalsIgnoreCase(status.trim()) ||
          taskStatus.displayName.equalsIgnoreCase(status.trim())) {
        return taskStatus;
      }
    }

    throw new IllegalArgumentException("Invalid task status: " + status +
        ". Valid values are: TO_DO, IN_PROGRESS, DONE");
  }
}
