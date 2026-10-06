package log;

import model.Action;
import model.Resource;
import model.User;

public class Logger {

    public static synchronized void logAccess(User user, Resource resource, Action action, boolean allowed) {
        LogEntry<Resource> entry = new LogEntry<Resource>(resource, user.getId(), user.getRole(),
                resource.getName(), action, allowed);
        System.out.println(entry);
    }
}
