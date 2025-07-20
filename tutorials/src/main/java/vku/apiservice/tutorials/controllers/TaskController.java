package vku.apiservice.tutorials.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vku.apiservice.tutorials.config.PreAuthorizeUtil;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.dtos.TaskDto;
import vku.apiservice.tutorials.dtos.UpdateTaskDto;
import vku.apiservice.tutorials.entities.Task;
import vku.apiservice.tutorials.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize(PreAuthorizeUtil.ALL_AUTHENTICATED)
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER)
    public TaskDto createTask(@RequestBody @Valid CreateTaskDto data) {
        Task task = taskService.create(data);
        return taskService.convertToDto(task);
    }

    @PutMapping("/{id}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskService.isTaskOwnerById(#id, authentication.name)")
    public TaskDto updateTask(@PathVariable("id") String id, @RequestBody @Valid UpdateTaskDto data) {
        Task task = taskService.updateTask(id, data);
        return taskService.convertToDto(task);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskService.isTaskOwnerById(#id, authentication.name)")
    public TaskDto patchTask(@PathVariable("id") String id, @RequestBody @Valid UpdateTaskDto data) {
        Task task = taskService.updateTask(id, data);
        return taskService.convertToDto(task);
    }

    @GetMapping()
    public Iterable<TaskDto> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable("id") String id) {
        Task task = taskService.getTaskById(id);
        return taskService.convertToDto(task);
    }

    @GetMapping("/assignee/{assigneeId}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskService.isTaskOwner(#assigneeId, authentication.name)")
    public Iterable<TaskDto> getTasksByAssignee(@PathVariable("assigneeId") String assigneeId) {
        return taskService.getTasksByAssignee(assigneeId);
    }

    @GetMapping("/project/{projectId}")
    public Iterable<TaskDto> getTasksByProject(@PathVariable("projectId") String projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskService.isTaskOwnerById(#id, authentication.name)")
    public ResponseEntity<String> deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}