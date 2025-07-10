package vku.apiservice.tutorials.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
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
    public Task createTask(@RequestBody @Valid CreateTaskDto data) {
        return taskService.createRequest(data);
    }

    @GetMapping()
    public Iterable<Task> getTasks() {
        return taskService.getTasks();
    }

}