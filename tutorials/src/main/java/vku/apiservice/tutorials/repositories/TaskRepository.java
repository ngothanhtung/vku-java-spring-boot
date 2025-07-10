package vku.apiservice.tutorials.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vku.apiservice.tutorials.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {


}