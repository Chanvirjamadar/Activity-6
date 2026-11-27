package model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Concrete Student class extending Person.
 * 
 * Demonstrates Key OOP Concepts:
 * 1. Inheritance: Extends Person to inherit general personal attributes and behaviors.
 * 2. Polymorphism: Implements abstract methods (getDisplayRole, getSummary) and overrides Object methods.
 * 3. Encapsulation: Protects student-specific attributes with strict validation setters.
 * 4. Constructor Chaining: Uses super(...) to invoke Person constructors.
 */
public class Student extends Person implements Comparable<Student> {
    private static final long serialVersionUID = 1L;

    // Student-specific encapsulated attributes
    private String studentId;
    private String rollNumber;
    private String department;
    private String yearSemester;

    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{2,20}$");
    private static final Pattern ROLL_PATTERN = Pattern.compile("^[A-Za-z0-9/_-]{2,25}$");

    /**
     * Default constructor (OOP: Constructor Overloading).
     */
    public Student() {
        super();
        this.studentId = "";
        this.rollNumber = "";
        this.department = "General";
        this.yearSemester = "1st Year (Sem 1)";
    }

    /**
     * Full Parameterized Constructor.
     * Invokes the superclass constructor using 'super' keyword.
     *
     * @param studentId    Unique student identifier (e.g., STU-1001)
     * @param fullName     Student's full name
     * @param rollNumber   Academic roll number (e.g., CS-2024-045)
     * @param department   Academic department
     * @param yearSemester Current academic year and semester
     * @param email        Valid email address
     * @param mobileNumber 10-digit mobile number
     * @param gender       Gender (Male, Female, Other)
     * @throws ValidationException on validation constraint failure
     */
    public Student(String studentId, String fullName, String rollNumber,
                   String department, String yearSemester, String email,
                   String mobileNumber, String gender) throws ValidationException {
        super(fullName, email, mobileNumber, gender);
        setStudentId(studentId);
        setRollNumber(rollNumber);
        setDepartment(department);
        setYearSemester(yearSemester);
    }

    /**
     * Copy Constructor (OOP: Constructor Overloading).
     *
     * @param other The existing Student object to replicate
     */
    public Student(Student other) {
        super(other);
        if (other != null) {
            this.studentId = other.studentId;
            this.rollNumber = other.rollNumber;
            this.department = other.department;
            this.yearSemester = other.yearSemester;
        }
    }

    // ==========================================
    // Polymorphic Method Overriding (@Override)
    // ==========================================

    @Override
    public String getDisplayRole() {
        return "Student";
    }

    @Override
    public String getSummary() {
        return String.format("[%s] %s | Roll No: %s | Dept: %s | Year: %s",
                studentId, getFullName(), rollNumber, department, yearSemester);
    }

    // ==========================================
    // Method Overloading (OOP)
    // ==========================================

    /**
     * Updates student details using individual parameters.
     */
    public void updateDetails(String fullName, String rollNumber, String department,
                              String yearSemester, String email, String mobileNumber,
                              String gender) throws ValidationException {
        setFullName(fullName);
        setRollNumber(rollNumber);
        setDepartment(department);
        setYearSemester(yearSemester);
        setEmail(email);
        setMobileNumber(mobileNumber);
        setGender(gender);
    }

    /**
     * Updates student details by copying from another Student object (Method Overloading).
     */
    public void updateDetails(Student source) throws ValidationException {
        if (source == null) {
            throw new ValidationException("Source student cannot be null.");
        }
        updateDetails(source.getFullName(), source.getRollNumber(), source.getDepartment(),
                source.getYearSemester(), source.getEmail(), source.getMobileNumber(),
                source.getGender());
    }

    // ==========================================
    // Encapsulation: Getters and Setters with Validation
    // ==========================================

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty.");
        }
        String trimmed = studentId.trim().toUpperCase();
        if (!ID_PATTERN.matcher(trimmed).matches()) {
            throw new ValidationException("Student ID must be 2-20 alphanumeric characters (letters, numbers, hyphens).");
        }
        this.studentId = trimmed;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) throws ValidationException {
        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            throw new ValidationException("Roll Number cannot be empty.");
        }
        String trimmed = rollNumber.trim();
        if (!ROLL_PATTERN.matcher(trimmed).matches()) {
            throw new ValidationException("Roll Number must be 2-25 alphanumeric characters (e.g., CS-2024-001).");
        }
        this.rollNumber = trimmed;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) throws ValidationException {
        if (department == null || department.trim().isEmpty()) {
            throw new ValidationException("Department selection cannot be empty.");
        }
        this.department = department.trim();
    }

    public String getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(String yearSemester) throws ValidationException {
        if (yearSemester == null || yearSemester.trim().isEmpty()) {
            throw new ValidationException("Year / Semester selection cannot be empty.");
        }
        this.yearSemester = yearSemester.trim();
    }

    // ==========================================
    // CSV Serialization Helpers
    // ==========================================

    /**
     * Converts student data into a CSV formatted string.
     */
    public String toCsvRow() {
        return escapeCsv(studentId) + "," +
               escapeCsv(getFullName()) + "," +
               escapeCsv(rollNumber) + "," +
               escapeCsv(department) + "," +
               escapeCsv(yearSemester) + "," +
               escapeCsv(getEmail()) + "," +
               escapeCsv(getMobileNumber()) + "," +
               escapeCsv(getGender());
    }

    /**
     * Parses a CSV row into a Student object.
     */
    public static Student fromCsvRow(String csvLine) throws ValidationException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        // Split handling escaped commas or simple CSV split
        String[] parts = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (parts.length < 8) {
            throw new ValidationException("Invalid CSV record format: " + csvLine);
        }

        String id = unescapeCsv(parts[0]);
        String name = unescapeCsv(parts[1]);
        String roll = unescapeCsv(parts[2]);
        String dept = unescapeCsv(parts[3]);
        String yearSem = unescapeCsv(parts[4]);
        String email = unescapeCsv(parts[5]);
        String phone = unescapeCsv(parts[6]);
        String gender = unescapeCsv(parts[7]);

        return new Student(id, name, roll, dept, yearSem, email, phone, gender);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String unescapeCsv(String value) {
        if (value == null) return "";
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).replace("\"\"", "\"");
        }
        return trimmed;
    }

    // ==========================================
    // Standard Overrides
    // ==========================================

    @Override
    public int compareTo(Student o) {
        if (o == null) return 1;
        // Natural ordering by Student ID
        return this.studentId.compareToIgnoreCase(o.studentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", department='" + department + '\'' +
                ", yearSemester='" + yearSemester + '\'' +
                ", email='" + getEmail() + '\'' +
                ", mobileNumber='" + getMobileNumber() + '\'' +
                ", gender='" + getGender() + '\'' +
                '}';
    }
}

