package eu.learn.ro;

// Model Layer
public class Task {
    private final String id;
    private final String description;
    private final int priority;

    public Task(String id, String description, int priority) {
        this.id = id;
        this.description = description;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Taks{");
        sb.append("id='").append(id).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}
