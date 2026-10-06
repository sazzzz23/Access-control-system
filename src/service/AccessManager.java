package service;

import model.Action;
import model.Capability;
import model.Read;
import model.Resource;
import model.Role;
import model.SecurityLevel;
import model.User;
import model.Write;
import log.Logger;

public class AccessManager {

    private static final AccessManager instance = new AccessManager();

    private AccessManager() {
    }

    public static AccessManager getInstance() {
        return instance;
    }

    public boolean canAccess(User user, Resource resource) {
        return canAccess(user, resource, Action.READ);
    }

    public boolean canAccess(User user, Resource resource, Action action) {
        if (user == null || resource == null || action == null) {
            throw new IllegalArgumentException("User, resource and action cannot be null");
        }

        boolean allowed = false;

        if (action == Action.READ) {
            allowed = canRead(user, resource);
        } else if (action == Action.WRITE) {
            allowed = canWrite(user);
        }

        Logger.logAccess(user, resource, action, allowed);
        return allowed;
    }

    public Capability<Read> getReadCapability(User user, Resource resource) {
        if (canAccess(user, resource, Action.READ)) {
            // Only issue a typed capability after the access check passes.
            return Capability.forRead(resource.getName());
        }
        return null;
    }

    public Capability<Write> getWriteCapability(User user, Resource resource) {
        if (canAccess(user, resource, Action.WRITE)) {
            // Write capability is only given to users who pass the write rule.
            return Capability.forWrite(resource.getName());
        }
        return null;
    }

    private boolean canRead(User user, Resource resource) {
        Role role = user.getRole();
        SecurityLevel level = resource.getLevel();

        if (role == Role.ADMIN) {
            return true;
        }
        if (role == Role.GUEST) {
            return level == SecurityLevel.PUBLIC;
        }
        if (role == Role.STUDENT || role == Role.STAFF) {
            return level == SecurityLevel.PUBLIC || level == SecurityLevel.INTERNAL;
        }
        return false;
    }

    private boolean canWrite(User user) {
        return user.getRole() == Role.ADMIN;
    }
}
