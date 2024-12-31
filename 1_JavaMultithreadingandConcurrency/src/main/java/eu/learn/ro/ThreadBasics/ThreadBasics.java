package eu.learn.ro.ThreadBasics;

public class ThreadBasics {
    public static void main(String[] args) {
        Thread t1 = new Thread(new PrintTask("Task 1"));
        Thread t2 = new Thread(new PrintTask("Task 2"));

        t1.start();
        t2.start();
    }
}
