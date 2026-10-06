package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Capability;
import model.Read;
import model.Resource;
import model.Role;
import model.SecurityLevel;
import model.User;
import model.Write;
import service.AccessManager;

public class AccessManagerTest {

    @Test
    void testGuestAllowed() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u1", "Bob", Role.GUEST);
        Resource res = new Resource("Website", SecurityLevel.PUBLIC);

        Capability<Read> cap = manager.getReadCapability(user, res);

        assertNotNull(cap);
        assertEquals("Website", cap.getResourceName());
    }

    @Test
    void testGuestDenied() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u1", "Bob", Role.GUEST);
        Resource res = new Resource("Portal", SecurityLevel.INTERNAL);

        assertNull(manager.getReadCapability(user, res));
    }

    @Test
    void testStudentAllowed() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u2", "Alice", Role.STUDENT);
        Resource res = new Resource("Portal", SecurityLevel.INTERNAL);

        Capability<Read> cap = manager.getReadCapability(user, res);

        assertNotNull(cap);
        assertEquals("Portal", cap.getResourceName());
    }

    @Test
    void testStudentDeniedOnConfidential() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u2", "Alice", Role.STUDENT);
        Resource res = new Resource("Reports", SecurityLevel.CONFIDENTIAL);

        assertNull(manager.getReadCapability(user, res));
        assertNull(manager.getWriteCapability(user, res));
    }

    @Test
    void testStaffDenied() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u3", "Staff1", Role.STAFF);
        Resource res = new Resource("Reports", SecurityLevel.CONFIDENTIAL);

        assertNull(manager.getReadCapability(user, res));
    }

    @Test
    void testAdminAllowed() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u4", "Admin", Role.ADMIN);
        Resource res = new Resource("Secret", SecurityLevel.CONFIDENTIAL);

        Capability<Read> readCap = manager.getReadCapability(user, res);
        Capability<Write> writeCap = manager.getWriteCapability(user, res);

        assertNotNull(readCap);
        assertNotNull(writeCap);
    }

    @Test
    void testReadCapability() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u2", "Alice", Role.STUDENT);
        Resource res = new Resource("Lecture", SecurityLevel.INTERNAL);

        Capability<Read> cap = manager.getReadCapability(user, res);

        assertNotNull(cap);
        assertEquals("Lecture", cap.getResourceName());
    }

    @Test
    void testWriteCapability() {
        AccessManager manager = AccessManager.getInstance();
        User user = new User("u4", "Admin", Role.ADMIN);
        Resource res = new Resource("Exam Paper", SecurityLevel.CONFIDENTIAL);

        Capability<Write> writeCap = manager.getWriteCapability(user, res);
        assertNotNull(writeCap);
        res.write(writeCap, "Updated paper");

        Capability<Read> readCap = manager.getReadCapability(user, res);
        assertNotNull(readCap);

        assertEquals("Updated paper", res.read(readCap));
    }

    @Test
    void testNullInput() {
        AccessManager manager = AccessManager.getInstance();
        Resource res = new Resource("Portal", SecurityLevel.PUBLIC);

        assertThrows(IllegalArgumentException.class, () -> manager.getReadCapability(null, res));
    }

    @Test
    void testInvalidResource() {
        assertThrows(IllegalArgumentException.class, () -> new Resource("", SecurityLevel.PUBLIC));
        assertThrows(IllegalArgumentException.class, () -> new Resource("Portal", null));
    }
}
