package vku.apiservice.tutorials.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.dtos.AssigneeDto;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.dtos.TaskDto;
import vku.apiservice.tutorials.entities.Task;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.TaskRepository;
import vku.apiservice.tutorials.repositories.UserRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task create(CreateTaskDto data) {
        User user = userRepository.findById(data.getAssigneeId()).orElseThrow(
                () -> new HttpException("User not found with id: " + data.getAssigneeId(), HttpStatus.BAD_REQUEST));

        Task task = new Task();
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());
        task.setStatus(data.getStatus());
        task.setPriority(data.getPriority());
        task.setAssignee(user);

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
            existingTask.setStatus(data.getStatus());
        }
        if (data.getPriority() != null && !data.getPriority().trim().isEmpty()) {
            existingTask.setPriority(data.getPriority());
        }
        if (data.getAssigneeId() != null) {
            User user = userRepository.findById(data.getAssigneeId())
                    .orElseThrow(() -> new HttpException("User not found with id: " + data.getAssigneeId(),
                            HttpStatus.BAD_REQUEST));
            existingTask.setAssignee(user);
        }

        return taskRepository.save(existingTask);
    }
}