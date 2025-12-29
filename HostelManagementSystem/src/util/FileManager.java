package util;

import model.Allocation;
import model.Room;
import model.Student;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static final String STUDENT_FILE = "students.dat";
    private static final String ROOM_FILE = "rooms.dat";
    private static final String ALLOCATION_FILE = "allocations.dat";

    /* ===================== STUDENTS ===================== */

    public static ArrayList<Student> loadStudents() {
        return loadList(STUDENT_FILE);
    }

    public static void saveStudents(ArrayList<Student> students) {
        saveList(STUDENT_FILE, students);
    }

    /* ===================== ROOMS ===================== */

    public static ArrayList<Room> loadRooms() {
        return loadList(ROOM_FILE);
    }

    public static void saveRooms(ArrayList<Room> rooms) {
        saveList(ROOM_FILE, rooms);
    }

    /* ===================== ALLOCATIONS ===================== */

    public static ArrayList<Allocation> loadAllocations() {
        return loadList(ALLOCATION_FILE);
    }

    public static void saveAllocations(ArrayList<Allocation> allocations) {
        saveList(ALLOCATION_FILE, allocations);
    }

    /* ===================== GENERIC METHODS ===================== */

    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> loadList(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList<?>) {
                return (ArrayList<T>) obj;
            }
        } catch (Exception e) {
            System.err.println("Failed to load file: " + fileName);
            System.err.println("Reason: " + e.getMessage());
        }

        // Safe fallback
        return new ArrayList<>();
    }

    private static <T> void saveList(String fileName, ArrayList<T> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(list);
        } catch (IOException e) {
            System.err.println("Failed to save file: " + fileName);
            System.err.println("Reason: " + e.getMessage());
        }
    }
}
