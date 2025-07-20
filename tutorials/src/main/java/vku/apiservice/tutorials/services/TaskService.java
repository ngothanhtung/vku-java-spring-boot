package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.dtos.AssigneeDto;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.dtos.ProjectSummaryDto;
import vku.apiservice.tutorials.dtos.TaskDto;
import vku.apiservice.tutorials.dtos.UpdateTaskDto;
import vku.apiservice.tutorials.entities.Project;
import vku.apiservice.tutorials.entities.Task;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.enums.TaskPriority;
import vku.apiservice.tutorials.enums.TaskStatus;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.ProjectRepository;
import vku.apiservice.tutorials.repositories.TaskRepository;
import vku.apiservice.tutorials.repositories.UserRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository,
            ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task create(CreateTaskDto data) {
        User user = userRepository.findById(data.getAssigneeId()).orElseThrow(
                () -> new HttpException("User not found with id: " + data.getAssigneeId(), HttpStatus.BAD_REQUEST));

        Task task = new Task();
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());

        // Convert String to enum with defaults if null/empty
        if (data.getStatus() != null && !data.getStatus().trim().isEmpty()) {
            task.setStatus(TaskStatus.fromString(data.getStatus()));
        }
        if (data.getPriority() != null && !data.getPriority().trim().isEmpty()) {
            task.setPriority(TaskPriority.fromString(data.getPriority()));
        }

        task.setAssignee(user);

        // Handle optional project assignment
        if (data.getProjectId() != null && !data.getProjectId().trim().isEmpty()) {
            Project project = projectRepository.findById(data.getProjectId())
                    .orElseThrow(() -> new HttpException("Project not found with id: " + data.getProjectId(),
                            HttpStatus.BAD_REQUEST));
            task.setProject(project);
        }

        return this.taskRepository.save(task); // @PrePersist will set defaults if needed
    }

    public List<TaskDto> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return this.convertToDtoList(tasks);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new HttpException("Task not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    // Get by assignee
    public List<TaskDto> getTasksByAssignee(String assigneeId) {
        Optional<User> user = userRepository.findById(assigneeId);
        if (user.isEmpty()) {
            throw new HttpException("User not found with id: " + assigneeId, HttpStatus.NOT_FOUND);
        }

        List<Task> tasks = taskRepository.findAllTasksWithAssignee().stream()
                .filter(task -> task.getAssignee() != null && task.getAssignee().getId().equals(assigneeId))
                .toList();
        return this.convertToDtoList(tasks);
    }

    // Get tasks by project
    public List<TaskDto> getTasksByProject(String projectId) {
        // Verify project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new HttpException("Project not found with id: " + projectId, HttpStatus.NOT_FOUND));

        List<Task> tasks = taskRepository.findAll().stream()
                .filter(task -> task.getProject() != null && task.getProject().getId().equals(projectId))
                .toList();
        return this.convertToDtoList(tasks);
    }

    // change status
    public Task changeTaskStatus(String id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new HttpException("Task not found with id: " + id, HttpStatus.NOT_FOUND));
        TaskStatus taskStatus = TaskStatus.fromString(status);
        task.setStatus(taskStatus);
        return taskRepository.save(task);
    }

    public Task changeTaskPriority(String id, String priority) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new HttpException("Task not found with id: " + id, HttpStatus.NOT_FOUND));
        TaskPriority taskPriority = TaskPriority.fromString(priority);
        task.setPriority(taskPriority);
        return taskRepository.save(task);
    }

    public TaskDto convertToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedDate(task.getCompletedDate());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());

        // Convert User to AssigneeDto to exclude audit fields
        if (task.getAssignee() != null) {
            User assignee = task.getAssignee();
            dto.setAssignee(new AssigneeDto(
                    assignee.getId(),
                    assignee.getName(),
                    assignee.getEmail()));
        }

        // Convert Project to ProjectSummaryDto if exists
        if (task.getProject() != null) {
            Project project = task.getProject();
            dto.setProject(new ProjectSummaryDto(
                    project.getId(),
                    project.getName(),
                    project.getDescription()));
        }

        // Map audit fields
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setCreatedBy(task.getCreatedBy());
        dto.setUpdatedBy(task.getUpdatedBy());

        return dto;
    }

    public List<TaskDto> convertToDtoList(List<Task> tasks) {
        return tasks.stream().map(this::convertToDto).toList();
    }

    public void deleteTask(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new HttpException("Task not found with id: " + id, HttpStatus.NOT_FOUND));
        taskRepository.delete(task);
    }

    public Task updateTask(String id, CreateTaskDto data) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new HttpException("Task not found with id: " + id, HttpStatus.NOT_FOUND));

        if (data.getTitle() != null) {
            existingTask.setTitle(data.getTitle());
        }
        if (data.getDescription() != null) {
            existingTask.setDescription(data.getDescription());
        }
        if (data.getStatus() != null && !data.getStatus().trim().isEmpty()) {
            existingTask.setStatus(TaskStatus.fromString(data.getStatus()));
        }
        if (data.getPriority() != null && !data.getPriority().trim().isEmpty()) {
            existingTask.setPriority(TaskPriority.fromString(data.getPriority()));
        }
        if (data.getAssigneeId() != null) {
            User user = userRepository.findById(data.getAssigneeId())
                    .orElseThrow(() -> new HttpException("User not found with id: " + data.getAssigneeId(),
                            HttpStatus.BAD_REQUEST));
            existingTask.setAssignee(user);
        }

        return taskRepository.save(existingTask);
    }

    /**
     * Updates a task using UpdateTaskDto with improved validation and error
     * handling
     */
    public Task updateTask(String id, UpdateTaskDto data) {
        // Validate that at least one field is provided
        if (!data.hasAnyField()) {
            throw new HttpException("At least one field must be provided for update", HttpStatus.BAD_REQUEST);
        }

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new HttpException("Task not found with id: " + id, HttpStatus.NOT_FOUND));

        // Update title if provided and valid
        if (data.hasValidTitle()) {
            existingTask.setTitle(data.getTitle().trim());
        }

        // Update description if provided and valid
        if (data.hasValidDescription()) {
            existingTask.setDescription(data.getDescription().trim());
        }

        // Update status if provided and valid
        if (data.hasValidStatus()) {
            existingTask.setStatus(TaskStatus.fromString(data.getStatus()));
        }

        // Update priority if provided and valid
        if (data.hasValidPriority()) {
            existingTask.setPriority(TaskPriority.fromString(data.getPriority()));
        }

        // Update assignee if provided and valid
        if (data.hasValidAssigneeId()) {
            User user = userRepository.findById(data.getAssigneeId())
                    .orElseThrow(() -> new HttpException("User not found with id: " + data.getAssigneeId(),
                            HttpStatus.BAD_REQUEST));
            existingTask.setAssignee(user);
        }

        // Update project if provided
        if (data.getProjectId() != null) {
            if (data.hasValidProjectId()) {
                // Assign to project
                Project project = projectRepository.findById(data.getProjectId())
                        .orElseThrow(() -> new HttpException("Project not found with id: " + data.getProjectId(),
                                HttpStatus.BAD_REQUEST));
                existingTask.setProject(project);
            } else {
                // Remove from project (empty string or just spaces)
                existingTask.setProject(null);
            }
        }

        return taskRepository.save(existingTask);
    }

    /**
     * Check if the given user is the owner/assignee of tasks by assignee ID
     * Used for RBAC authorization
     */
    public boolean isTaskOwner(String assigneeId, String currentUserEmail) {
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        if (!currentUser.isPresent()) {
            return false;
        }

        // Check if the current user is the assignee
        return currentUser.get().getId().equals(assigneeId);
    }

    /**
     * Check if the given user is the owner/assignee of a specific task by task ID
     * Used for RBAC authorization
     */
    public boolean isTaskOwnerById(String taskId, String currentUserEmail) {
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        if (!currentUser.isPresent()) {
            return false;
        }

        Optional<Task> task = taskRepository.findById(taskId);
        if (!task.isPresent()) {
            return false;
        }

        // Check if the current user is the assignee of this task
        return task.get().getAssignee() != null &&
                task.get().getAssignee().getId().equals(currentUser.get().getId());
    }
}
