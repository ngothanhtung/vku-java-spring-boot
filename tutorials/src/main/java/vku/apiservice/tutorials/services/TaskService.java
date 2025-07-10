package vku.apiservice.tutorials.services;

import org.springframework.stereotype.Service;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.entities.*;
import vku.apiservice.tutorials.repositories.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

    }

    public Task createRequest(CreateTaskDto data) {
        Task task = new Task();
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());

        return this.taskRepository.save(task);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
}