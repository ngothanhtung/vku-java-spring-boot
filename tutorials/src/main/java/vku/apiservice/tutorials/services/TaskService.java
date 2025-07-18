package vku.apiservice.tutorials.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.dtos.TaskDto;
import vku.apiservice.tutorials.entities.*;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.TaskRepository;
import vku.apiservice.tutorials.repositories.UserRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    public TaskService(TaskRepository taskRepository,  UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;

    }

    public Task createRequest(CreateTaskDto data) {
        User user = userRepository.findById(data.getAssigneeId()).orElseThrow(() -> new HttpException("User not found with id: " + data.getAssigneeId(), HttpStatus.BAD_REQUEST));

        Task task = new Task();
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());

        task.setAssignee(user);
        return this.taskRepository.save(task);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
}