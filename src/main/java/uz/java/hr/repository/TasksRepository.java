package uz.java.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.hr.entity.Task;
import uz.java.hr.entity.User;
import uz.java.hr.enums.TaskStatus;

import java.util.Optional;
import java.util.UUID;

public interface TasksRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByTaskTakerAndName(User taskTaker, String name);

    Optional<Task> findByTaskTakerAndTaskStatus(User xodim, TaskStatus completed);
}
