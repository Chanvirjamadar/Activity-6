package dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import model.Student;
import model.ValidationException;

/**
 * Handles persistent file storage for student records using CSV format.
 * 
 * Demonstrates:
 * 1. Data Persistence: Automatically saves and loads records to/from a local file.
 * 2. Robust File I/O: Creates parent directories, uses buffered streams, handles exceptions cleanly.
 * 3. Seed Initialization: Automatically generates realistic sample students when starting for the first time.
 */
public class FileHandler {

    private static final String DEFAULT_FILE_PATH = "data/students.csv";
    private static final String CSV_HEADER = "StudentId,FullName,RollNumber,Department,YearSemester,Email,MobileNumber,Gender";
    
    private final String filePath;

    /**
     * Default constructor using the standard path: data/students.csv with auto-seeding enabled.
     */
    public FileHandler() {
        this(DEFAULT_FILE_PATH, true);
    }

    /**
     * Constructor allowing custom file path (seeds only if it's the default file).
     */
    public FileHandler(String filePath) {
        this(filePath, DEFAULT_FILE_PATH.equals(filePath));
    }

    /**
     * Constructor allowing custom file path and explicit control over auto-seeding.
     */
    public FileHandler(String filePath, boolean autoSeed) {
        this.filePath = (filePath != null && !filePath.trim().isEmpty()) ? filePath.trim() : DEFAULT_FILE_PATH;
        ensureDirectoryAndFileExist(autoSeed);
    }

    /**
     * Ensures data directory and file exist. If file is brand new or empty and autoSeed is true, seeds sample records.
     */
    private void ensureDirectoryAndFileExist(boolean autoSeed) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!file.exists() || file.length() == 0) {
                if (autoSeed) {
                    seedInitialSampleData(file);
                } else {
                    // Create empty file with header
                    saveStudentsToFile(file, new ArrayList<>());
                }
            }
        } catch (Exception e) {
            System.err.println("[FileHandler] Notice initializing storage file: " + e.getMessage());
        }
    }

    /**
     * Seeds realistic sample student records so the application has initial data out-of-the-box.
     */
    private void seedInitialSampleData(File file) {
        List<Student> sampleList = new ArrayList<>();
        try {
            sampleList.add(new Student("STU-1001", "Aarav Sharma", "CS-2023-001",
                    "Computer Science", "3rd Year (Sem 5)", "aarav.sharma@college.edu", "9876543210", "Male"));
            sampleList.add(new Student("STU-1002", "Priya Patel", "IT-2023-014",
                    "Information Technology", "3rd Year (Sem 5)", "priya.patel@college.edu", "9812345678", "Female"));
            sampleList.add(new Student("STU-1003", "Rohan Verma", "ME-2022-032",
                    "Mechanical Engineering", "4th Year (Sem 7)", "rohan.verma@college.edu", "9765432109", "Male"));
            sampleList.add(new Student("STU-1004", "Ananya Deshmukh", "EC-2024-008",
                    "Electronics & Comm", "2nd Year (Sem 3)", "ananya.d@college.edu", "9898989898", "Female"));
            sampleList.add(new Student("STU-1005", "Kabir Singh", "AI-2024-021",
                    "AI & Data Science", "2nd Year (Sem 3)", "kabir.singh@college.edu", "9123456780", "Male"));
            sampleList.add(new Student("STU-1006", "Sneha Reddy", "CE-2023-019",
                    "Civil Engineering", "3rd Year (Sem 5)", "sneha.reddy@college.edu", "9345678901", "Female"));
            sampleList.add(new Student("STU-1007", "Vikram Malhotra", "EE-2022-005",
                    "Electrical Engineering", "4th Year (Sem 7)", "vikram.m@college.edu", "9456789012", "Male"));
            sampleList.add(new Student("STU-1008", "Ishita Joshi", "CS-2025-055",
                    "Computer Science", "1st Year (Sem 1)", "ishita.j@college.edu", "9567890123", "Female"));

            saveStudentsToFile(file, sampleList);
            System.out.println("[FileHandler] Successfully seeded " + sampleList.size() + " sample student records to " + filePath);
        } catch (ValidationException e) {
            System.err.println("[FileHandler] Error seeding sample data: " + e.getMessage());
        }
    }

    /**
     * Reads all student records from the CSV file.
     *
     * @return List of Student objects loaded from file
     */
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return students;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // Header line
            // If the first line doesn't start with StudentId, it might be data, so process it
            if (line != null && !line.startsWith("StudentId")) {
                try {
                    Student s = Student.fromCsvRow(line);
                    if (s != null) students.add(s);
                } catch (ValidationException ve) {
                    System.err.println("[FileHandler] Skipping corrupted line: " + line);
                }
            }

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    Student s = Student.fromCsvRow(line);
                    if (s != null) {
                        students.add(s);
                    }
                } catch (ValidationException ve) {
                    System.err.println("[FileHandler] Skipping corrupted record: " + line + " (Reason: " + ve.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] IO Error loading student records from " + filePath + ": " + e.getMessage());
        }

        return students;
    }

    /**
     * Saves the provided list of students to the CSV file.
     * Uses atomic write via a temporary file to avoid file corruption if interrupted.
     *
     * @param students List of students to persist
     * @return true if write succeeded, false otherwise
     */
    public synchronized boolean saveStudents(List<Student> students) {
        File targetFile = new File(filePath);
        return saveStudentsToFile(targetFile, students);
    }

    private synchronized boolean saveStudentsToFile(File targetFile, List<Student> students) {
        File tempFile = new File(targetFile.getAbsolutePath() + ".tmp");
        try {
            File parentDir = targetFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))) {

                writer.write(CSV_HEADER);
                writer.newLine();

                if (students != null) {
                    for (Student student : students) {
                        if (student != null) {
                            writer.write(student.toCsvRow());
                            writer.newLine();
                        }
                    }
                }
            }

            // Atomic file replacement
            if (targetFile.exists()) {
                if (!targetFile.delete()) {
                    System.err.println("[FileHandler] Warning: Could not delete old file before rename.");
                }
            }

            if (!tempFile.renameTo(targetFile)) {
                // Fallback copy if renameTo fails across file systems
                copyFile(tempFile, targetFile);
                tempFile.delete();
            }

            return true;
        } catch (IOException e) {
            System.err.println("[FileHandler] IO Error saving students: " + e.getMessage());
            return false;
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private void copyFile(File src, File dest) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
