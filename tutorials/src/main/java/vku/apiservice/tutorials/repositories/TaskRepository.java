package vku.apiservice.tutorials.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vku.apiservice.tutorials.entities.Task;
import vku.apiservice.tutorials.entities.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.assignee u")
    List<Task> findAllTasksWithRoles();
}