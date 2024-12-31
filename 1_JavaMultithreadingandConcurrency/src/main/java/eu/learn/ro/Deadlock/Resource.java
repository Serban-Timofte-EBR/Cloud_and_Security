package eu.learn.ro.Deadlock;

public class Resource {
    public void doSomething() {
        System.out.println(Thread.currentThread().getName() + " is working.");
    }
}
