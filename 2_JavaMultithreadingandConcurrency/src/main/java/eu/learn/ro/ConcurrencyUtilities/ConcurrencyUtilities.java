package eu.learn.ro.ConcurrencyUtilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyUtilities {
    public static void main(String[] args) {
        // 1. Thread Reusability: Threads are reused, avoiding the overhead of creating and destroying threads for each task.
        // 2. Better Resource Management: The framework optimizes thread usage, ensuring system resources are not overwhelmed.
        // 3. Simplified Thread Management: You don’t need to handle thread lifecycle manually (e.g., starting, joining).
        // 4. Concurrency Features: Provides advanced features like scheduling and delayed execution.
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 6; i++) {
            int taskId = i;
//            execute()
//	•	Accepts a Runnable task and executes it.
//	•	Does not return a result or allow tracking the task’s outcome.
//            submit()
//	•	Accepts either a Runnable or a Callable task.
//	•	Returns a Future object, which can:
//	•	Track the status of the task (e.g., completed, canceled).
//	•	Retrieve the result (if using a Callable).
//	•	Handle exceptions thrown during task execution.
            executorService.submit(() -> {
                System.out.println("Task ID: " + taskId + " performed by " + Thread.currentThread().getName());
            });
        }

        // Previously submitted tasks will complete, but no new tasks will be accepted.
        executorService.shutdown();
    }
}
