package vku.apiservice.tutorials.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.dtos.TaskDto;
import vku.apiservice.tutorials.entities.Task;
import vku.apiservice.tutorials.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public Task create(@RequestBody @Valid CreateTaskDto data) {
        return taskService.create(data);
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

}