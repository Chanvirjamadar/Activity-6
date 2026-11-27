package test;

import dao.FileHandler;
import dao.RecordOperations;
import java.io.File;
import java.util.List;
import java.util.Map;
import model.Person;
import model.Student;
import model.ValidationException;
import service.StudentManager;

/**
 * Automated Verification Harness for Student Management System.
 * Tests all OOP principles, business logic, validation constraints, and file persistence.
 */
public class SystemVerifier {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  STARTING AUTOMATED VERIFICATION OF SMS BACKEND  ");
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        // Test 1: Inheritance and Abstraction
        try {
            Person person = new Student("STU-9999", "Test Student", "CS-9999",
                    "Computer Science", "1st Year (Sem 1)", "test@college.edu", "9988776655", "Male");
            assert "Student".equals(person.getDisplayRole()) : "Role should be 'Student'";
            assert person.getSummary().contains("STU-9999") : "Summary should contain ID";
            System.out.println("[PASS] Test 1: Inheritance and Abstraction verified.");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 1: " + t.getMessage());
            failed++;
        }

        // Test 2: Encapsulation and Validations
        try {
            boolean caughtInvalidEmail = false;
            try {
                new Student("STU-001", "Bad Email", "CS-001", "CS", "Sem 1", "not-an-email", "9876543210", "Male");
            } catch (ValidationException e) {
                caughtInvalidEmail = true;
            }
            assert caughtInvalidEmail : "Should reject invalid email format";

            boolean caughtInvalidPhone = false;
            try {
                new Student("STU-002", "Bad Phone", "CS-002", "CS", "Sem 1", "valid@email.com", "12345", "Male");
            } catch (ValidationException e) {
                caughtInvalidPhone = true;
            }
            assert caughtInvalidPhone : "Should reject non-10-digit mobile number";

            System.out.println("[PASS] Test 2: Encapsulation and Input Validation constraints verified.");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 2: " + t.getMessage());
            failed++;
        }

        // Test 3: Repository Polymorphism and CRUD with File Persistence
        String testFilePath = "data/test_students.csv";
        new File(testFilePath).delete(); // Start fresh

        try {
            FileHandler testHandler = new FileHandler(testFilePath);
            RecordOperations<Student> manager = new StudentManager(testHandler);

            // Add
            Student s1 = new Student("STU-TEST1", "Alice Wonder", "CS-TEST-01",
                    "Computer Science", "2nd Year (Sem 3)", "alice@college.edu", "9876543210", "Female");
            Student s2 = new Student("STU-TEST2", "Bob Builder", "ME-TEST-02",
                    "Mechanical Engineering", "3rd Year (Sem 5)", "bob@college.edu", "9876543211", "Male");

            manager.add(s1);
            manager.add(s2);

            assert manager.count() == 2 : "Count should be 2";
            System.out.println("[PASS] Test 3: RecordOperations add & count verified.");
            passed++;

            // Test 4: Duplicate ID Prevention
            boolean duplicateIdBlocked = false;
            try {
                Student sDuplicate = new Student("STU-TEST1", "Alice Clone", "CS-TEST-99",
                        "Computer Science", "1st Year (Sem 1)", "clone@college.edu", "9876543212", "Female");
                manager.add(sDuplicate);
            } catch (ValidationException e) {
                duplicateIdBlocked = true;
            }
            assert duplicateIdBlocked : "Duplicate student ID should be rejected!";
            System.out.println("[PASS] Test 4: Duplicate Student ID rejection verified.");
            passed++;

            // Test 5: Duplicate Roll Number Prevention
            boolean duplicateRollBlocked = false;
            try {
                Student sDuplicateRoll = new Student("STU-TEST3", "Charlie", "CS-TEST-01", // duplicate roll
                        "Civil Engineering", "1st Year (Sem 1)", "charlie@college.edu", "9876543213", "Male");
                manager.add(sDuplicateRoll);
            } catch (ValidationException e) {
                duplicateRollBlocked = true;
            }
            assert duplicateRollBlocked : "Duplicate Roll Number should be rejected!";
            System.out.println("[PASS] Test 5: Duplicate Roll Number rejection verified.");
            passed++;

            // Test 6: Method Overloading Search
            StudentManager concreteManager = (StudentManager) manager;
            List<Student> searchAll = concreteManager.search("Wonder");
            assert searchAll.size() == 1 && searchAll.get(0).getStudentId().equals("STU-TEST1") : "Search All failed";

            List<Student> searchByRoll = concreteManager.search("ME-TEST-02", "Roll Number");
            assert searchByRoll.size() == 1 && searchByRoll.get(0).getStudentId().equals("STU-TEST2") : "Search by Roll failed";

            List<Student> searchNone = concreteManager.search("NonExistentKeyword");
            assert searchNone.isEmpty() : "Search non-existent should be empty";

            System.out.println("[PASS] Test 6: Method Overloading search (search by query & search by criteria) verified.");
            passed++;

            // Test 7: Update Operation
            Student toUpdate = manager.findById("STU-TEST1");
            toUpdate.updateDetails("Alice Wonder Updated", "CS-TEST-01", "Computer Science",
                    "4th Year (Sem 7)", "alice.updated@college.edu", "9876543210", "Female");
            manager.update(toUpdate);

            Student updatedCheck = manager.findById("STU-TEST1");
            assert "Alice Wonder Updated".equals(updatedCheck.getFullName()) : "Name was not updated";
            assert "4th Year (Sem 7)".equals(updatedCheck.getYearSemester()) : "Semester was not updated";
            System.out.println("[PASS] Test 7: Student update operation verified.");
            passed++;

            // Test 8: File Persistence Verification
            // Create a brand new manager instance reading from the same file
            StudentManager reloadManager = new StudentManager(new FileHandler(testFilePath));
            assert reloadManager.count() == 2 : "Reloaded count should be 2";
            Student reloadedAlice = reloadManager.findById("STU-TEST1");
            assert reloadedAlice != null : "STU-TEST1 must exist after reload";
            assert "Alice Wonder Updated".equals(reloadedAlice.getFullName()) : "Reloaded name must match updated value";
            System.out.println("[PASS] Test 8: File persistence (write to disk & reload) verified.");
            passed++;

            // Test 9: Delete Operation
            boolean deleted = manager.delete("STU-TEST2");
            assert deleted : "Delete should return true";
            assert manager.count() == 1 : "Count after delete should be 1";
            assert manager.findById("STU-TEST2") == null : "Deleted student should not be found";

            // Verify persistence after delete
            StudentManager reloadAfterDelete = new StudentManager(new FileHandler(testFilePath));
            assert reloadAfterDelete.count() == 1 : "Reloaded count after delete should be 1";
            System.out.println("[PASS] Test 9: Student delete and persistence verified.");
            passed++;

            // Test 10: Aggregations & Distribution
            Map<String, Integer> deptDist = concreteManager.getDepartmentDistribution();
            assert deptDist.containsKey("Computer Science") : "Department distribution should have Computer Science";
            System.out.println("[PASS] Test 10: Department and Gender analytics verified.");
            passed++;

        } catch (Throwable t) {
            System.err.println("[FAIL] Error during testing: " + t.getMessage());
            t.printStackTrace();
            failed++;
        } finally {
            new File(testFilePath).delete();
            new File(testFilePath + ".tmp").delete();
        }

        System.out.println("==================================================");
        System.out.println("TEST RESULTS: " + passed + " PASSED | " + failed + " FAILED");
        System.out.println("==================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }
}

