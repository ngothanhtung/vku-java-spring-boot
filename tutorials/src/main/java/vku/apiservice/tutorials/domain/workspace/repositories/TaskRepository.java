package vku.apiservice.tutorials.domain.workspace.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.domain.workspace.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.assignee u")
    List<Task> findAllTasksWithAssignee();
}