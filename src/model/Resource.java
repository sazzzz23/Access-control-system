package model;

public class Resource {
    private final String name;
    private final SecurityLevel level;
    private String data;

    public Resource(String name, SecurityLevel level) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource name cannot be empty");
        }
        if (level == null) {
            throw new IllegalArgumentException("Security level cannot be null");
        }
        this.name = name;
        this.level = level;
        this.data = "";
    }

    public String getName() {
        return name;
    }

    public SecurityLevel getLevel() {
        return level;
    }

    // Read needs a Read capability
    public String read(Capability<Read> cap) {
        if (cap == null || !cap.matches(name)) {
            throw new IllegalArgumentException("Valid read capability required");
        }
        return data;
    }

    // Write needs a Write capability
    public void write(Capability<Write> cap, String newData) {
        if (cap == null || !cap.matches(name)) {
            throw new IllegalArgumentException("Valid write capability required");
        }
        this.data = newData;
    }
}
