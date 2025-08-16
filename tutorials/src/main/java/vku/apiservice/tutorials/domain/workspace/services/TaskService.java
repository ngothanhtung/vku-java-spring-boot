package vku.apiservice.tutorials.domain.workspace.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.domain.common.exceptions.EntityNotFoundException;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;
import vku.apiservice.tutorials.domain.workspace.dtos.AssigneeResponseDto;
import vku.apiservice.tutorials.domain.workspace.dtos.CreateTaskRequestDto;
import vku.apiservice.tutorials.domain.workspace.dtos.ProjectResponseDto;
import vku.apiservice.tutorials.domain.workspace.dtos.TaskResponseDto;
import vku.apiservice.tutorials.domain.workspace.dtos.UpdateTaskRequestDto;
import vku.apiservice.tutorials.domain.workspace.entities.Project;
import vku.apiservice.tutorials.domain.workspace.entities.Task;
import vku.apiservice.tutorials.domain.workspace.enums.TaskPriority;
import vku.apiservice.tutorials.domain.workspace.enums.TaskStatus;
import vku.apiservice.tutorials.domain.workspace.repositories.ProjectRepository;
import vku.apiservice.tutorials.domain.workspace.repositories.TaskRepository;

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

    public Task create(CreateTaskRequestDto data) {
        User user = userRepository.findById(data.getAssigneeId()).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + data.getAssigneeId()));

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
                    .orElseThrow(
                            () -> new EntityNotFoundException("Project not found with id: " + data.getProjectId()));
            task.setProject(project);
        }

        return this.taskRepository.save(task); // @PrePersist will set defaults if needed
    }

    public List<TaskResponseDto> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return this.convertToDtoList(tasks);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    // Get by assignee
    public List<TaskResponseDto> getTasksByAssignee(String assigneeId) {
        Optional<User> user = userRepository.findById(assigneeId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found with id: " + assigneeId);
        }

        List<Task> tasks = taskRepository.findAllTasksWithAssignee().stream()
                .filter(task -> task.getAssignee() != null && task.getAssignee().getId().equals(assigneeId))
                .toList();
        return this.convertToDtoList(tasks);
    }

    // Get tasks by project
    public List<TaskResponseDto> getTasksByProject(String projectId) {
        // Verify project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        List<Task> tasks = taskRepository.findAll().stream()
                .filter(task -> task.getProject() != null && task.getProject().getId().equals(projectId))
                .toList();
        return this.convertToDtoList(tasks);
    }

    // change status
    public Task changeTaskStatus(String id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        TaskStatus taskStatus = TaskStatus.fromString(status);
        task.setStatus(taskStatus);
        return taskRepository.save(task);
    }

    public Task changeTaskPriority(String id, String priority) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        TaskPriority taskPriority = TaskPriority.fromString(priority);
        task.setPriority(taskPriority);
        return taskRepository.save(task);
    }

    public TaskResponseDto convertToDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedDate(task.getCompletedDate());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());

        // Convert User to AssigneeResponseDto to exclude audit fields
        if (task.getAssignee() != null) {
            User assignee = task.getAssignee();
            dto.setAssignee(new AssigneeResponseDto(
                    assignee.getId(),
                    assignee.getName(),
                    assignee.getEmail()));
        }

        // Convert Project to ProjectResponseDto if exists
        if (task.getProject() != null) {
            Project project = task.getProject();
            dto.setProject(new ProjectResponseDto(
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

    public List<TaskResponseDto> convertToDtoList(List<Task> tasks) {
        return tasks.stream().map(this::convertToDto).toList();
    }

    public void deleteTask(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        this.taskRepository.delete(task);
    }

    public Task updateTask(String id, CreateTaskRequestDto data) {
        Task existingTask = this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

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
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + data.getAssigneeId()));
            existingTask.setAssignee(user);
        }

        return taskRepository.save(existingTask);
    }

    /**
     * Updates a task using UpdateTaskRequestDto with improved validation and error
     * handling
     */
    public Task updateTask(String id, UpdateTaskRequestDto data) {

        Task existingTask = this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        // Update the title if provided and valid
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
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + data.getAssigneeId()));
            existingTask.setAssignee(user);
        }

        // Update the project if provided
        if (data.getProjectId() != null) {
            if (data.hasValidProjectId()) {
                // Assign to a project
                Project project = projectRepository.findById(data.getProjectId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Project not found with id: " + data.getProjectId()));
                existingTask.setProject(project);
            } else {
                // Remove from the project (empty string or just spaces)
                existingTask.setProject(null);
            }
        }

        return this.taskRepository.save(existingTask);
    }

    /**
     * Check if the given user is the owner/assignee of tasks by assignee ID
     * Used for RBAC authorization
     */
    public boolean isTaskOwner(String assigneeId, String currentUserEmail) {
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        return currentUser.map(user -> user.getId().equals(assigneeId)).orElse(false);

        // Check if the current user is the assignee
    }

    /**
     * Check if the given user is the owner/assignee of a specific task by task ID
     * Used for RBAC authorization
     */
    public boolean isTaskOwnerById(String taskId, String currentUserEmail) {
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser.isEmpty()) {
            return false;
        }

        Optional<Task> task = this.taskRepository.findById(taskId);
        return task.filter(value -> value.getAssignee() != null &&
                value.getAssignee().getId().equals(currentUser.get().getId())).isPresent();

        // Check if the current user is the assignee of this task
    }
}
