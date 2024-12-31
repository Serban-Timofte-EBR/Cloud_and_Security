package eu.learn.ro.ThreadBasics;

public class PrintTask implements Runnable{
    private final String message;

    public PrintTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": "  + message);
    }
}
