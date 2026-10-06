package test;

import model.Capability;
import model.Read;
import model.Resource;
import model.Role;
import model.SecurityLevel;
import model.User;
import model.Write;
import service.AccessManager;

public class MainTest {

    public static void main(String[] args) {
        AccessManager manager = AccessManager.getInstance();

        User student = new User("u1", "Mo", Role.STUDENT);
        User staff = new User("u2", "Jane", Role.STAFF);
        User admin = new User("u3", "Sam", Role.ADMIN);
        User guest = new User("u0", "Guest", Role.GUEST);

        Resource lecture = new Resource("Lecture recordings", SecurityLevel.INTERNAL);
        Resource exam = new Resource("Exam Paper", SecurityLevel.CONFIDENTIAL);
        Resource website = new Resource("University Website", SecurityLevel.PUBLIC);

        // Preload a little data so the read demo prints something visible.
        Capability<Write> websiteWrite = manager.getWriteCapability(admin, website);
        if (websiteWrite != null) {
            website.write(websiteWrite, "University home page");
        }

        Capability<Write> lectureWrite = manager.getWriteCapability(admin, lecture);
        if (lectureWrite != null) {
            lecture.write(lectureWrite, "Lecture notes");
        }

        // Three threads make different access attempts at the same time.
        Thread studentThread = new Thread(() -> {
            Capability<Read> read = manager.getReadCapability(student, lecture);
            System.out.println("Student read lecture: " + (read != null ? lecture.read(read) : "REFUSED"));

            Capability<Write> write = manager.getWriteCapability(student, exam);
            System.out.println("Student write exam: " + (write != null ? "ALLOWED" : "REFUSED"));
        }, "student-thread");

        Thread staffThread = new Thread(() -> {
            Capability<Read> read = manager.getReadCapability(staff, exam);
            System.out.println("Staff read exam: " + (read != null ? "ALLOWED" : "REFUSED"));

            Capability<Read> websiteRead = manager.getReadCapability(staff, website);
            System.out.println("Staff read website: " + (websiteRead != null ? website.read(websiteRead) : "REFUSED"));
        }, "staff-thread");

        Thread adminThread = new Thread(() -> {
            Capability<Write> write = manager.getWriteCapability(admin, exam);
            if (write != null) {
                exam.write(write, "Exam paper content");
            }
            System.out.println("Admin write exam: " + (write != null ? "ALLOWED" : "REFUSED"));

            Capability<Read> read = manager.getReadCapability(admin, exam);
            System.out.println("Admin read exam: " + (read != null ? exam.read(read) : "REFUSED"));
        }, "admin-thread");

        studentThread.start();
        staffThread.start();
        adminThread.start();

        // Wait for the demo output before the program exits.
        try {
            studentThread.join();
            staffThread.join();
            adminThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Capability<Read> guestWebsite = manager.getReadCapability(guest, website);
        System.out.println("Guest read website: " + (guestWebsite != null ? website.read(guestWebsite) : "REFUSED"));

        Capability<Read> guestExam = manager.getReadCapability(guest, exam);
        System.out.println("Guest read exam: " + (guestExam != null ? "ALLOWED" : "REFUSED"));
    }
}
