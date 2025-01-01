package eu.learn.ro.controller;

import eu.learn.ro.model.Task;
import eu.learn.ro.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable long id) {
        return taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PostMapping
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @PatchMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        return taskService.saveTask(task);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return "Task deleted";
    }
}
