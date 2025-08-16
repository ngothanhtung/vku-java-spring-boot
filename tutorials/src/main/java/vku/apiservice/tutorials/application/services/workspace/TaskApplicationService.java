package vku.apiservice.tutorials.application.services.workspace;

import java.util.List;

import org.springframework.stereotype.Service;

import vku.apiservice.tutorials.application.dtos.workspace.CreateTaskRequestDto;
import vku.apiservice.tutorials.application.dtos.workspace.TaskResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.UpdateTaskRequestDto;
import vku.apiservice.tutorials.application.mappers.TaskMapper;
import vku.apiservice.tutorials.domain.workspace.entities.Task;
import vku.apiservice.tutorials.domain.workspace.services.TaskService;

@Service
public class TaskApplicationService {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskApplicationService(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDto createTask(CreateTaskRequestDto data) {
        Task task = taskService.createTask(
                data.getTitle(),
                data.getDescription(),
                data.getStatus(),
                data.getPriority(),
                data.getAssigneeId(),
                data.getProjectId()
        );
        return taskMapper.toTaskResponseDto(task);
    }

    public List<TaskResponseDto> getTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
    }

    public TaskResponseDto getTaskById(String id) {
        Task task = taskService.getTaskById(id);
        return taskMapper.toTaskResponseDto(task);
    }

    public List<TaskResponseDto> getTasksByAssignee(String assigneeId) {
        List<Task> tasks = taskService.getTasksByAssignee(assigneeId);
        return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
    }

    public List<TaskResponseDto> getTasksByProject(String projectId) {
        List<Task> tasks = taskService.getTasksByProject(projectId);
        return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
    }

    public TaskResponseDto changeTaskStatus(String id, String status) {
        Task task = taskService.changeTaskStatus(id, status);
        return taskMapper.toTaskResponseDto(task);
    }

    public TaskResponseDto changeTaskPriority(String id, String priority) {
        Task task = taskService.changeTaskPriority(id, priority);
        return taskMapper.toTaskResponseDto(task);
    }

    public TaskResponseDto updateTask(String id, UpdateTaskRequestDto data) {
        Task task = taskService.updateTask(
                id,
                data.getTitle(),
                data.getDescription(),
                data.getStatus(),
                data.getPriority(),
                data.getAssigneeId(),
                data.getProjectId()
        );
        return taskMapper.toTaskResponseDto(task);
    }

    public void deleteTask(String id) {
        taskService.deleteTask(id);
    }

    // Authorization helper methods for @PreAuthorize
    public boolean isTaskOwner(String assigneeId, String currentUserEmail) {
        return taskService.isTaskOwner(assigneeId, currentUserEmail);
    }

    public boolean isTaskOwnerById(String taskId, String currentUserEmail) {
        return taskService.isTaskOwnerById(taskId, currentUserEmail);
    }
}