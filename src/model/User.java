package model;

public class User {
    private final String username;
    private final String id;
    private final Role role;

    public User(String id, String username, Role role) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User id cannot be empty");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
