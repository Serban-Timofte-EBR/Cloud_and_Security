package eu.learn.ro;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    @Test
    void testAddAndRetrieveTask() {
        TaskManager manager = new TaskManager();

        // Add a task
        Task task = new Task("1", "Test Task", 1);
        manager.addTask(task);

        // Retreive the task with the highest priority
        Task highPriorityTask = manager.getNextTask();

        // Verify the task is correct
        assertNotNull(highPriorityTask, "Task should not be null");
        assertEquals("Test Task", highPriorityTask.getDescription(), "Task description should match");
        assertEquals(1, highPriorityTask.getPriority(), "Task priority should match");
    }

    @Test
    void testRemoveTask() {
        TaskManager manager = new TaskManager();

        // Add a task
        Task task = new Task("1", "Task to Remove", 2);
        manager.addTask(task);

        // Remove the task
        manager.removeTask("1");

        // Verify the task is removed from both the map and the queue
        assertNull(manager.getNextTask(), "Queue should be empty after removing the task");
        assertTrue(manager.tasks.isEmpty(), "Task map should be empty after removal");
    }

    @Test
    void testPriorityRetrieval() {
        TaskManager manager = new TaskManager();

        // Add tasks with different priorities
        manager.addTask(new Task("1", "Low Priority", 3));
        manager.addTask(new Task("2", "High Priority", 1));
        manager.addTask(new Task("3", "Medium Priority", 2));

        // Retrieve tasks in priority order
        assertEquals("High Priority", manager.getNextTask().getDescription(), "Task with the highest priority should be retrieved first");
        assertEquals("Medium Priority", manager.getNextTask().getDescription(), "Task with the second highest priority should be retrieved next");
        assertEquals("Low Priority", manager.getNextTask().getDescription(), "Task with the lowest priority should be retrieved last");
    }
}
