package vku.apiservice.tutorials.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vku.apiservice.tutorials.dtos.CreateRoleDto;
import vku.apiservice.tutorials.dtos.CreateTaskDto;
import vku.apiservice.tutorials.entities.*;
import vku.apiservice.tutorials.exceptions.HttpException;
import vku.apiservice.tutorials.repositories.RoleRepository;
import vku.apiservice.tutorials.repositories.TaskRepository;
import vku.apiservice.tutorials.repositories.UserRepository;
import vku.apiservice.tutorials.repositories.UserRoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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