package log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.Action;
import model.Role;

public class LogEntry<T> {
    private final LocalDateTime timestamp;
    private final String userId;
    private final Role role;
    private final String resourceName;
    private final Action action;
    private final boolean allowed;
    private final T item;

    public LogEntry(T item, String userId, Role role, String resourceName, Action action, boolean allowed) {
        this.item = item;
        this.timestamp = LocalDateTime.now();
        this.userId = userId;
        this.role = role;
        this.resourceName = resourceName;
        this.action = action;
        this.allowed = allowed;
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        String time = timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String decision = allowed ? "ALLOW" : "REFUSE";
        return time + ", " + userId + ", " + role + ", " + resourceName + ", " + action + ", " + decision;
    }
}
