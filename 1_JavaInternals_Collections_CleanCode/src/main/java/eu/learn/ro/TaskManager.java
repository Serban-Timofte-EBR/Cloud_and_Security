package eu.learn.ro;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

// Service Layer

/**
 * TaskManager is a service class that manages tasks in a thread-safe way.
 * This means that any piece of code, function or data-structure ensures consistent behaviour when accessed by multiple threads simultaneously. This means no corruption of data, no race conditions, no deadlocks, etc.
 */
public class TaskManager {
    // TASK STORAGE: Keeps all tasks stored by their unique IDs.
    public final Map<String, Task> tasks = new ConcurrentHashMap<>();  // ConcurrentHashMap is a thread-safe implementation of the Map interface

    // PRIORITY QUEUE: Manages tasks sorted by their priority for efficient retrieval.
    // Why PriorityBlockingQueue? It is a thread-safe priority queue = Heap
    // with higher priority are retrieved first in a concurrent environment.
    public final PriorityBlockingQueue<Task> priorityQueue = new PriorityBlockingQueue<>(10, Comparator.comparingInt(Task::getPriority));

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        priorityQueue.add(task);
    }

    /**
     * Retrieves and removes the next task with the highest priority.
     * @return The next task, or null if no tasks are available.
     */
    public Task getNextTask() {
        return priorityQueue.poll();
    }

    public void removeTask(String taskId) {
        Task task = tasks.remove(taskId);

        if (task != null) {
            priorityQueue.remove(task);
        }
    }

    public void listTasks() {
        tasks.values().forEach(System.out::println);
    }
}
