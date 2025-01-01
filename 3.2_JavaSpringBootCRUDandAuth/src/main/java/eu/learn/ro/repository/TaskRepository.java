package eu.learn.ro.repository;

import eu.learn.ro.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository: Provides ready-to-use CRUD methods like
// save(), findAll(), findById(), and deleteById()
// for Task entity.
public interface TaskRepository extends JpaRepository<Task, Long> {
}
