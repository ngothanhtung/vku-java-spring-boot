package vku.apiservice.tutorials.domain.workspace.repositories;

import vku.apiservice.tutorials.domain.workspace.entities.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(String id);
    boolean existsByTitle(String title);
    Task save(Task task);
    void delete(Task task);
    List<Task> findAllTasksWithAssignee();
}
