package service;

import dao.FileHandler;
import dao.RecordOperations;
import java.util.*;
import java.util.stream.Collectors;
import model.Student;
import model.ValidationException;

/**
 * Service class managing student business logic, validation, searching, filtering, and persistence.
 * 
 * Demonstrates Key OOP Concepts:
 * 1. Abstraction & Polymorphism: Implements RecordOperations<Student>.
 * 2. Encapsulation: Maintains an internal private list and lookup indices.
 * 3. Method Overloading: Provides multiple overloaded search and filter methods.
 * 4. Business Logic Validation: Prevents duplicate IDs, duplicate Roll Numbers, and enforces constraints.
 */
public class StudentManager implements RecordOperations<Student> {

    private final List<Student> students;
    private final FileHandler fileHandler;

    /**
     * Default constructor: uses default FileHandler and loads existing records.
     */
    public StudentManager() {
        this(new FileHandler());
    }

    /**
     * Parameterized constructor allowing custom FileHandler dependency injection.
     */
    public StudentManager(FileHandler fileHandler) {
        this.fileHandler = Objects.requireNonNull(fileHandler, "FileHandler cannot be null.");
        this.students = new ArrayList<>();
        reloadFromStorage();
    }

    /**
     * Reloads student records from the persistent file storage.
     */
    public synchronized void reloadFromStorage() {
        students.clear();
        List<Student> loaded = fileHandler.loadStudents();
        if (loaded != null) {
            students.addAll(loaded);
        }
        // Keep sorted by Student ID by default
        Collections.sort(students);
    }

    /**
     * Persists the current memory list to the file storage.
     */
    private synchronized boolean persist() {
        return fileHandler.saveStudents(students);
    }

    // ==========================================
    // Implementation of RecordOperations<Student> (OOP: Polymorphism & Abstraction)
    // ==========================================

    @Override
    public synchronized boolean add(Student student) throws ValidationException {
        if (student == null) {
            throw new ValidationException("Cannot add a null student record.");
        }

        // Rule 1: Check duplicate Student ID (Case-insensitive)
        if (existsById(student.getStudentId())) {
            throw new ValidationException("Student with ID '" + student.getStudentId() + "' already exists! IDs must be unique.");
        }

        // Rule 2: Check duplicate Roll Number (Case-insensitive)
        if (existsByRollNumber(student.getRollNumber(), null)) {
            throw new ValidationException("Roll Number '" + student.getRollNumber() + "' is already assigned to another student!");
        }

        // Add and sort
        students.add(student);
        Collections.sort(students);

        // Auto-save to file
        boolean saved = persist();
        if (!saved) {
            System.err.println("[StudentManager] Warning: Student added in memory but failed to save to file.");
        }
        return true;
    }

    @Override
    public synchronized boolean update(Student updatedStudent) throws ValidationException {
        if (updatedStudent == null) {
            throw new ValidationException("Cannot update with a null student record.");
        }

        int index = indexOfId(updatedStudent.getStudentId());
        if (index == -1) {
            throw new ValidationException("Student with ID '" + updatedStudent.getStudentId() + "' does not exist.");
        }

        // Check if new roll number conflicts with ANY OTHER student
        if (existsByRollNumber(updatedStudent.getRollNumber(), updatedStudent.getStudentId())) {
            throw new ValidationException("Roll Number '" + updatedStudent.getRollNumber() + "' is already assigned to another student!");
        }

        // Update target record
        Student existing = students.get(index);
        existing.updateDetails(updatedStudent);

        // Auto-save to file
        boolean saved = persist();
        if (!saved) {
            System.err.println("[StudentManager] Warning: Student updated in memory but failed to save to file.");
        }
        return true;
    }

    @Override
    public synchronized boolean delete(String studentId) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty for deletion.");
        }

        int index = indexOfId(studentId.trim());
        if (index == -1) {
            return false; // Not found
        }

        students.remove(index);

        // Auto-save to file
        persist();
        return true;
    }

    @Override
    public synchronized Student findById(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        String cleanId = studentId.trim();
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(cleanId)) {
                return new Student(s); // Return defensive copy (Encapsulation)
            }
        }
        return null;
    }

    @Override
    public synchronized List<Student> getAll() {
        // Return a defensive copy to protect internal list encapsulation
        List<Student> copy = new ArrayList<>(students.size());
        for (Student s : students) {
            copy.add(new Student(s));
        }
        return copy;
    }

    @Override
    public synchronized int count() {
        return students.size();
    }

    // ==========================================
    // Method Overloading: Search & Filter Features (OOP)
    // ==========================================

    /**
     * Universal Search (Overloaded Method 1).
     * Searches across Student ID, Full Name, Roll Number, and Department.
     *
     * @param query Search keyword
     * @return List of matching students
     */
    public synchronized List<Student> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAll();
        }
        String q = query.trim().toLowerCase();
        return students.stream()
                .filter(s -> s.getStudentId().toLowerCase().contains(q) ||
                             s.getFullName().toLowerCase().contains(q) ||
                             s.getRollNumber().toLowerCase().contains(q) ||
                             s.getDepartment().toLowerCase().contains(q) ||
                             s.getEmail().toLowerCase().contains(q) ||
                             s.getMobileNumber().contains(q))
                .map(Student::new)
                .collect(Collectors.toList());
    }

    /**
     * Targeted Criteria Search (Overloaded Method 2).
     * Searches by a specific criterion such as "ID", "Name", "Roll Number", or "Department".
     *
     * @param query    Search keyword
     * @param searchBy Targeted attribute ("All", "ID", "Name", "Roll Number", "Department")
     * @return List of matching students
     */
    public synchronized List<Student> search(String query, String searchBy) {
        if (query == null || query.trim().isEmpty()) {
            return getAll();
        }
        if (searchBy == null || searchBy.equalsIgnoreCase("All")) {
            return search(query);
        }

        String q = query.trim().toLowerCase();
        return students.stream()
                .filter(s -> {
                    switch (searchBy.toUpperCase()) {
                        case "ID":
                        case "STUDENT ID":
                            return s.getStudentId().toLowerCase().contains(q);
                        case "NAME":
                        case "FULL NAME":
                            return s.getFullName().toLowerCase().contains(q);
                        case "ROLL NUMBER":
                        case "ROLL NO":
                            return s.getRollNumber().toLowerCase().contains(q);
                        case "DEPARTMENT":
                            return s.getDepartment().toLowerCase().contains(q);
                        case "YEAR":
                        case "SEMESTER":
                            return s.getYearSemester().toLowerCase().contains(q);
                        default:
                            return s.getStudentId().toLowerCase().contains(q) ||
                                   s.getFullName().toLowerCase().contains(q) ||
                                   s.getRollNumber().toLowerCase().contains(q);
                    }
                })
                .map(Student::new)
                .collect(Collectors.toList());
    }

    /**
     * Multi-Attribute Filter (Overloaded Method 3).
     * Filters students by Department and/or Year/Semester.
     *
     * @param department   Selected department ("All" for any)
     * @param yearSemester Selected year/semester ("All" for any)
     * @return Filtered list of students
     */
    public synchronized List<Student> filter(String department, String yearSemester) {
        boolean matchDept = (department != null && !department.equalsIgnoreCase("All") && !department.trim().isEmpty());
        boolean matchYear = (yearSemester != null && !yearSemester.equalsIgnoreCase("All") && !yearSemester.trim().isEmpty());

        if (!matchDept && !matchYear) {
            return getAll();
        }

        return students.stream()
                .filter(s -> {
                    boolean deptOk = !matchDept || s.getDepartment().equalsIgnoreCase(department.trim());
                    boolean yearOk = !matchYear || s.getYearSemester().equalsIgnoreCase(yearSemester.trim());
                    return deptOk && yearOk;
                })
                .map(Student::new)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Analytics and Aggregations
    // ==========================================

    /**
     * Computes the distribution of students grouped by Department.
     */
    public synchronized Map<String, Integer> getDepartmentDistribution() {
        Map<String, Integer> map = new TreeMap<>();
        for (Student s : students) {
            map.put(s.getDepartment(), map.getOrDefault(s.getDepartment(), 0) + 1);
        }
        return map;
    }

    /**
     * Computes the distribution of students grouped by Gender.
     */
    public synchronized Map<String, Integer> getGenderDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Male", 0);
        map.put("Female", 0);
        map.put("Other", 0);
        for (Student s : students) {
            String g = s.getGender();
            if (map.containsKey(g)) {
                map.put(g, map.get(g) + 1);
            } else {
                map.put("Other", map.get("Other") + 1);
            }
        }
        return map;
    }

    /**
     * Returns the most recently enrolled/added students up to a specified limit.
     */
    public synchronized List<Student> getRecentStudents(int limit) {
        int size = students.size();
        int count = Math.min(limit, size);
        List<Student> recent = new ArrayList<>(count);
        for (int i = size - 1; i >= size - count; i--) {
            recent.add(new Student(students.get(i)));
        }
        return recent;
    }

    // ==========================================
    // Helper Check Methods
    // ==========================================

    public synchronized boolean existsById(String studentId) {
        if (studentId == null) return false;
        String clean = studentId.trim();
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(clean)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean existsByRollNumber(String rollNumber, String excludeStudentId) {
        if (rollNumber == null) return false;
        String cleanRoll = rollNumber.trim();
        for (Student s : students) {
            if (excludeStudentId != null && s.getStudentId().equalsIgnoreCase(excludeStudentId.trim())) {
                continue; // Skip self check when updating
            }
            if (s.getRollNumber().equalsIgnoreCase(cleanRoll)) {
                return true;
            }
        }
        return false;
    }

    private int indexOfId(String studentId) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equalsIgnoreCase(studentId)) {
                return i;
            }
        }
        return -1;
    }

    public String getStoragePath() {
        return fileHandler.getFilePath();
    }
}

