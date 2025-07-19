package vku.apiservice.tutorials.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
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
    public TaskDto create(@RequestBody @Valid CreateTaskDto data) {
        Task task = taskService.create(data);
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

}