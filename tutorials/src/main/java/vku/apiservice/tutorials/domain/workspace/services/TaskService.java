package vku.apiservice.tutorials.domain.workspace.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.domain.common.exceptions.EntityNotFoundException;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;
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

    public Task createTask(String title, String description, String status, String priority,
            String assigneeId, String projectId) {
        // Validate assignee exists
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));

        // Create task entity
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setAssignee(assignee);

        // Set status with default if null/empty
        if (status != null && !status.trim().isEmpty()) {
            task.setStatus(TaskStatus.fromString(status));
        }

        // Set priority with default if null/empty
        if (priority != null && !priority.trim().isEmpty()) {
            task.setPriority(TaskPriority.fromString(priority));
        }

        // Handle optional project assignment
        if (projectId != null && !projectId.trim().isEmpty()) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
            task.setProject(project);
        }

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    public List<Task> getTasksByAssignee(String assigneeId) {
        // Validate assignee exists
        userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));

        return taskRepository.findAllTasksWithAssignee().stream()
                .filter(task -> task.getAssignee() != null && task.getAssignee().getId().equals(assigneeId))
                .toList();
    }

    public List<Task> getTasksByProject(String projectId) {
        // Validate project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        return taskRepository.findAll().stream()
                .filter(task -> task.getProject() != null && task.getProject().getId().equals(projectId))
                .toList();
    }

    public Task changeTaskStatus(String id, String status) {
        Task task = getTaskById(id);
        TaskStatus taskStatus = TaskStatus.fromString(status);
        task.setStatus(taskStatus);
        return taskRepository.save(task);
    }

    public Task changeTaskPriority(String id, String priority) {
        Task task = getTaskById(id);
        TaskPriority taskPriority = TaskPriority.fromString(priority);
        task.setPriority(taskPriority);
        return taskRepository.save(task);
    }

    public Task updateTask(String id, String title, String description, String status, String priority,
            String assigneeId, String projectId) {
        Task existingTask = getTaskById(id);

        // Update fields if provided
        if (title != null && !title.trim().isEmpty()) {
            existingTask.setTitle(title.trim());
        }

        if (description != null) {
            existingTask.setDescription(description.trim());
        }

        if (status != null && !status.trim().isEmpty()) {
            existingTask.setStatus(TaskStatus.fromString(status));
        }

        if (priority != null && !priority.trim().isEmpty()) {
            existingTask.setPriority(TaskPriority.fromString(priority));
        }

        if (assigneeId != null && !assigneeId.trim().isEmpty()) {
            User user = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));
            existingTask.setAssignee(user);
        }

        // Handle project assignment/removal
        if (projectId != null) {
            if (!projectId.trim().isEmpty()) {
                Project project = projectRepository.findById(projectId)
                        .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
                existingTask.setProject(project);
            } else {
                existingTask.setProject(null);
            }
        }

        return taskRepository.save(existingTask);
    }

    public void deleteTask(String id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    // Domain logic: Authorization checks
    public boolean isTaskOwner(String assigneeId, String currentUserEmail) {
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        return currentUser.map(user -> user.getId().equals(assigneeId)).orElse(false);
    }

    public boolean isTaskOwnerById(String taskId, String currentUserEmail) {
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser.isEmpty()) {
            return false;
        }

        Optional<Task> task = taskRepository.findById(taskId);
        return task.filter(value -> value.getAssignee() != null &&
                value.getAssignee().getId().equals(currentUser.get().getId())).isPresent();
    }    
}
