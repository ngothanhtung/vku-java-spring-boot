package vku.apiservice.tutorials.infrastructure.persistence.jpa.workspace;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vku.apiservice.tutorials.domain.workspace.entities.Task;
import vku.apiservice.tutorials.domain.workspace.repositories.TaskRepository;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, String>, TaskRepository {
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.assignee u")
    List<Task> findAllTasksWithAssignee();
}