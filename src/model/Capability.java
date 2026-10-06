package model;

public class Capability<T> {
    private final String resourceName;

    private Capability(String resourceName) {
        this.resourceName = resourceName;
    }

    public static Capability<Read> forRead(String resourceName) {
        return new Capability<Read>(resourceName);
    }

    public static Capability<Write> forWrite(String resourceName) {
        return new Capability<Write>(resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }

    public boolean matches(String name) {
        return resourceName != null && resourceName.equals(name);
    }
}
