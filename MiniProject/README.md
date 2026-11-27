# Student Management System (Java Swing OOP Application)

A desktop graphical application designed for college administrators to manage student records. Built using **pure Java and Java Swing**, incorporating core **Object-Oriented Programming (OOP)** principles, input validation, and real-time local file persistence.

---

## 1. Project Overview
- **Application Name**: Student Management System
- **Language**: Java ONLY (Java SE 8, 11, 17, 21, or 26+)
- **GUI Framework**: Java Swing (`javax.swing.*`, `java.awt.*`)
- **Data Persistence**: Java-based local file storage (`data/students.csv`) with automatic loading, atomic writing, and real-time synchronization.
- **IDE Compatibility**: Eclipse, IntelliJ IDEA, Apache NetBeans, and command-line execution (`javac` / `java`).

---

## 2. Project Directory Structure

```
MiniProject/
├── bin/                             # Compiled Java byte-code (.class files)
├── data/
│   └── students.csv                 # Persistent CSV storage file (pre-loaded with samples)
├── src/
│   ├── Main.java                    # Application entry point with EDT launcher & L&F setup
│   ├── model/
│   │   ├── Person.java              # Abstract base class (Abstraction & Inheritance base)
│   │   ├── Student.java             # Concrete Student entity (Inheritance, Encapsulation)
│   │   └── ValidationException.java  # Custom exception for input validation errors
│   ├── dao/
│   │   ├── RecordOperations.java    # Generic CRUD interface (Abstraction & Polymorphism)
│   │   └── FileHandler.java         # CSV file persistence manager & auto-seeder
│   ├── service/
│   │   └── StudentManager.java      # Business logic, duplicate checks, search overloading
│   ├── ui/
│   │   ├── UITheme.java             # Color palette (Navy, White, Light Blue) & fonts
│   │   ├── ModernButton.java        # Custom anti-aliased rounded button with hover effects
│   │   ├── ModernTable.java         # Custom JTable with alternating row colors & dark header
│   │   └── StudentGUI.java          # Responsive Swing GUI with CardLayout & modern sidebar
│   └── test/
│       └── SystemVerifier.java      # Automated verification harness for all OOP features
├── compile_and_run.bat              # One-click Windows compilation & launch script
├── compile_and_run.sh               # One-click Linux / macOS compilation & launch script
└── README.md                        # Complete project documentation and OOP explanation
```

---

## 3. Object-Oriented Programming (OOP) Implementation

This project implements core Object-Oriented Programming principles:

### A. Abstraction
- **Abstract Class (`model.Person`)**: Declares the template for human entities with shared attributes (`fullName`, `email`, `mobileNumber`, `gender`) and abstract methods:
  ```java
  public abstract String getDisplayRole();
  public abstract String getSummary();
  ```
- **Generic Interface (`dao.RecordOperations<T>`)**: Declares the public contract for CRUD operations (`add`, `update`, `delete`, `findById`, `getAll`, `count`) without exposing storage implementation details.

### B. Inheritance
- **Class Extension (`model.Student extends Person`)**: The `Student` class inherits personal attributes and validation behaviors from `Person`, adding college-specific attributes:
  - `studentId`
  - `rollNumber`
  - `department`
  - `yearSemester`
- **Super Keyword & Constructor Chaining**: `Student` constructors invoke `super(fullName, email, mobileNumber, gender)` to initialize inherited state.

### C. Encapsulation
- All data fields in `Person` and `Student` are strictly `private`.
- Access and mutation are controlled via `public` getters and validated setters.
- Mutating methods enforce strict validation rules:
  - Email format validation using regular expressions (`^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`).
  - Mobile number validation (exactly 10 numeric digits).
  - Student ID pattern (alphanumeric, 2–20 characters).
  - Roll number pattern (alphanumeric with hyphens/slashes, 2–25 characters).
  - Name characters (letters, spaces, dots, hyphens only).
- Defensive copying is used in `getAll()` and `findById()` to prevent external modification of internal collections.

### D. Polymorphism
- **Subtype Polymorphism (Interface-based)**:
  ```java
  RecordOperations<Student> manager = new StudentManager();
  Person student = new Student(...);
  ```
- **Method Overriding (`@Override`)**:
  - `Student.getDisplayRole()` overrides `Person.getDisplayRole()` to return `"Student"`.
  - `Student.getSummary()` provides student-specific formatting.
  - `Student.toString()`, `Student.equals(Object)`, and `Student.hashCode()` override standard `java.lang.Object` methods.
- **Method Overloading**:
  - **Constructors**: Default constructor, full parameterized constructor, and copy constructor.
  - **Search operations in `StudentManager`**:
    - `search(String query)`: Searches across all fields.
    - `search(String query, String criteria)`: Searches specifically by `"Student ID"`, `"Full Name"`, `"Roll Number"`, or `"Department"`.
  - **Update operations in `Student`**:
    - `updateDetails(String name, String roll, String dept, String year, String email, String mobile, String gender)`
    - `updateDetails(Student other)`

---

## 4. Key Application Features

### 1. Modern Dashboard
- Application title: **Student Management System**.
- **Metric Cards**:
  - Total Registered Students (live count from file/memory).
  - Total Academic Departments.
  - Male Students count.
  - Female Students count.
- **Quick Action Shortcuts**: Instant navigation to Add, View, and Search.
- **Recent Registrations Table**: Displays the 5 most recently enrolled students.

### 2. Add Student
- Form with fields:
  - Student ID (with optional **Auto ID** generator button: `STU-1001`, `STU-1002`, etc.)
  - Full Name
  - Roll Number
  - Department (Computer Science, Information Technology, AI & Data Science, Electronics & Comm, Mechanical, Civil, Electrical)
  - Year / Semester (1st Year Sem 1 through 4th Year Sem 8)
  - Email Address
  - Mobile Number (10 digits)
  - Gender (Male, Female, Other)
- **Validation**: Rejects duplicates, empty inputs, malformed emails, or invalid phone numbers.
- **Actions**: "Add Student" and "Clear Form".

### 3. View Students
- Interactive `ModernTable` with all student details.
- **Alternating Row Colors** (`#FFFFFF` and `#F8FAFC`).
- **Dark Navy Table Header** (`#0F2744`) with white bold text.
- **Live Filtering**:
  - Filter by Department.
  - Filter by Year / Semester.
  - Quick search box.
  - "Reset Filters" button.
- **Table Actions**:
  - "Refresh Table"
  - "Edit Selected" (loads selected student directly into the Update form)
  - "Delete Selected" (confirms and deletes selected student)

### 4. Update Student
- Search student by Student ID.
- Pre-populates all existing student details in editable fields (Student ID remains protected).
- Re-validates inputs and ensures the modified roll number does not conflict with any other student.
- Saves changes and immediately updates both in-memory list and local file storage.

### 5. Delete Student
- Search student by Student ID.
- Displays full preview card of the target record.
- **Confirmation Dialog**: Shows `JOptionPane.showConfirmDialog` before deletion.
- Removes student from memory and rewrites the CSV file safely.

### 6. Search Student
- Multi-criteria search dropdown:
  - `All Fields`
  - `Student ID`
  - `Full Name`
  - `Roll Number`
  - `Department`
- Instant search results table.
- Informative message when no matching student is found.

### 7. Analytics & Department Overview
- Table showing student enrollment count and percentage per department.
- Gender breakdown statistics (Male %, Female %, Other %).
- Storage engine details.

### 8. Application Exit Safeguard
- Confirmation dialog on window close or "Exit Application" button click to prevent accidental closure.

---

## 5. UI/UX Styling & Theme

- **Color Palette**:
  - Primary Dark Blue: `#0F2744` (Table headers, main titles)
  - Sidebar Navy: `#183153` (Sidebar navigation panel)
  - Active Blue: `#2563EB` (Primary buttons, active nav items)
  - Light Blue Accent: `#E0F2FE` / `#DBEAFE` (Row selection, subtle highlights)
  - Card & Background: `#FFFFFF` (Cards) and `#F8FAFC` (App background)
  - Accent Alerts: Emerald Green (`#10B981`), Rose Red (`#EF4444`), Amber Warning (`#F59E0B`)
- **Custom Components**:
  - `ModernButton`: Anti-aliased rounded rectangle buttons with responsive hover and press transitions.
  - `ModernTable`: Custom renderer with 34px row height, padded cells, centered column alignments, and zebra row striping.

---

## 6. How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher (JDK 11, 17, 21, or 26 recommended).
- Ensure `javac` and `java` are available in your system `PATH`.

---

### Option A: Using One-Click Scripts (Recommended)

#### On Windows:
Double-click `compile_and_run.bat` or run:
```cmd
compile_and_run.bat
```

#### On Linux / macOS:
Open terminal in the project directory and run:
```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

---

### Option B: Manual Command Line

#### 1. Compile all Java source files:
```cmd
mkdir bin
javac -encoding UTF-8 -d bin src/model/*.java src/dao/*.java src/service/*.java src/ui/*.java src/Main.java
```

#### 2. Run the application:
```cmd
java -cp bin Main
```

#### 3. Run the automated backend verification test:
```cmd
javac -encoding UTF-8 -d bin -cp bin src/test/SystemVerifier.java
java -ea -cp bin test.SystemVerifier
```

---

### Option C: Using IDEs

#### In Eclipse:
1. Open Eclipse and select **File -> New -> Java Project**.
2. Set Project Name: `StudentManagementSystem`.
3. Set the Project Location to this folder (`MiniProject`).
4. Ensure `src` is marked as the source folder.
5. Right-click `Main.java` -> **Run As -> Java Application**.

#### In IntelliJ IDEA:
1. Open IntelliJ IDEA and choose **File -> Open**.
2. Select the `MiniProject` folder.
3. Mark `src` as the **Sources Root** (Right-click `src` -> **Mark Directory as -> Sources Root**).
4. Open `src/Main.java` and click the green **Run** button.

#### In Apache NetBeans:
1. Open NetBeans and choose **File -> New Project -> Java with Ant / Java Application**.
2. Point source package folder to `src`.
3. Set Main Class to `Main`.
4. Click **Run Project**.

---

## 7. Pre-loaded Sample Data

The application comes pre-loaded with sample student records in `data/students.csv`:

| Student ID | Full Name | Roll Number | Department | Year / Semester | Email | Mobile | Gender |
|---|---|---|---|---|---|---|---|
| `STU-1001` | Aarav Sharma | `CS-2023-001` | Computer Science | 3rd Year (Sem 5) | `aarav.sharma@college.edu` | `9876543210` | Male |
| `STU-1002` | Priya Patel | `IT-2023-014` | Information Technology | 3rd Year (Sem 5) | `priya.patel@college.edu` | `9812345678` | Female |
| `STU-1003` | Rohan Verma | `ME-2022-032` | Mechanical Engineering | 4th Year (Sem 7) | `rohan.verma@college.edu` | `9765432109` | Male |
| `STU-1004` | Ananya Deshmukh | `EC-2024-008` | Electronics & Comm | 2nd Year (Sem 3) | `ananya.d@college.edu` | `9898989898` | Female |
| `STU-1005` | Kabir Singh | `AI-2024-021` | AI & Data Science | 2nd Year (Sem 3) | `kabir.singh@college.edu` | `9123456780` | Male |
| `STU-1006` | Sneha Reddy | `CE-2023-019` | Civil Engineering | 3rd Year (Sem 5) | `sneha.reddy@college.edu` | `9345678901` | Female |
| `STU-1007` | Vikram Malhotra | `EE-2022-005` | Electrical Engineering | 4th Year (Sem 7) | `vikram.m@college.edu` | `9456789012` | Male |
| `STU-1008` | Ishita Joshi | `CS-2025-055` | Computer Science | 1st Year (Sem 1) | `ishita.j@college.edu` | `9567890123` | Female |

---

## 8. Verification Results

All 10 automated test suites executed with 100% pass rate via `SystemVerifier`:
- `[PASS] Test 1: Inheritance and Abstraction verified.`
- `[PASS] Test 2: Encapsulation and Input Validation constraints verified.`
- `[PASS] Test 3: RecordOperations add & count verified.`
- `[PASS] Test 4: Duplicate Student ID rejection verified.`
- `[PASS] Test 5: Duplicate Roll Number rejection verified.`
- `[PASS] Test 6: Method Overloading search (by query & by criteria) verified.`
- `[PASS] Test 7: Student update operation verified.`
- `[PASS] Test 8: File persistence (write to disk & reload) verified.`
- `[PASS] Test 9: Student delete and persistence verified.`
- `[PASS] Test 10: Department and Gender analytics verified.`

