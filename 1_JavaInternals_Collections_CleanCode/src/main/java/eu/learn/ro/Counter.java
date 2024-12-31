package eu.learn.ro;

public class Counter {
    // AtomicInteger is thread safe
    public int count = 0;

    // or adding syncronized to increment method for thread safety
    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
