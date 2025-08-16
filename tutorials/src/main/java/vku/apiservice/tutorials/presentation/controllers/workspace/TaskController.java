package vku.apiservice.tutorials.presentation.controllers.workspace;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import vku.apiservice.tutorials.application.dtos.workspace.CreateTaskRequestDto;
import vku.apiservice.tutorials.application.dtos.workspace.TaskResponseDto;
import vku.apiservice.tutorials.application.dtos.workspace.UpdateTaskRequestDto;
import vku.apiservice.tutorials.application.services.workspace.TaskApplicationService;
import vku.apiservice.tutorials.infrastructure.config.PreAuthorizeUtil;

@RestController
@RequestMapping("/api/workspace/tasks")
public class TaskController {
    private final TaskApplicationService taskApplicationService;

    public TaskController(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
    }

    @PostMapping
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER)
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid CreateTaskRequestDto data) {
        TaskResponseDto task = taskApplicationService.createTask(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskApplicationService.isTaskOwnerById(#id, authentication.name)")
    public TaskResponseDto updateTask(@PathVariable("id") String id, @RequestBody @Valid UpdateTaskRequestDto data) {
        return taskApplicationService.updateTask(id, data);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskApplicationService.isTaskOwnerById(#id, authentication.name)")
    public TaskResponseDto patchTask(@PathVariable("id") String id, @RequestBody @Valid UpdateTaskRequestDto data) {
        return taskApplicationService.updateTask(id, data);
    }

    @GetMapping()
    public List<TaskResponseDto> getTasks() {
        return taskApplicationService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable("id") String id) {
        return taskApplicationService.getTaskById(id);
    }

    @GetMapping("/assignee/{assigneeId}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskApplicationService.isTaskOwner(#assigneeId, authentication.name)")
    public List<TaskResponseDto> getTasksByAssignee(@PathVariable("assigneeId") String assigneeId) {
        return taskApplicationService.getTasksByAssignee(assigneeId);
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponseDto> getTasksByProject(@PathVariable("projectId") String projectId) {
        return taskApplicationService.getTasksByProject(projectId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER + " or @taskApplicationService.isTaskOwnerById(#id, authentication.name)")
    public ResponseEntity<String> deleteTask(@PathVariable("id") String id) {
        taskApplicationService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}