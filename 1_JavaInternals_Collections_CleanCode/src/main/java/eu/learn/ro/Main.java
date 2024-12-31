package eu.learn.ro;

public class Main {
    public static void main(String[] args) {
        // Example of race condition
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // Wait for both threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter value: " + counter.getCount());
        // Both threads access and modify the count variable at the same time.
        // The operation count++ (read, increment, and write) is not atomic, so threads can overwrite each other’s changes.

        // Example of the Task class
        TaskManager taskManager = new TaskManager();

        // Adding tasks to the manager
        taskManager.addTask(new Task("1", "Fix bug #101", 2)); // Priority 2
        taskManager.addTask(new Task("2", "Develop new feature", 1)); // Priority 1
        taskManager.addTask(new Task("3", "Code review", 3)); // Priority 3

        // Displaying the tasks
        System.out.println("All tasks:");
        taskManager.listTasks();

        // Processing the tasks in priority order
        System.out.println("Processing tasks in priority order:");
        Task nextTask;
        while ((nextTask = taskManager.getNextTask()) != null) {
            System.out.println("Processing: " + nextTask);
        }

        // Attempt to remove a task and verify its removal.
        System.out.println("\nRemoving task with ID '2':");
        taskManager.removeTask("2");

        // List remaining tasks.
        System.out.println("\nRemaining tasks:");
        taskManager.listTasks();
    }
}