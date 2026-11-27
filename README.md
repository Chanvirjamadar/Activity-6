Student Management System
A Java Swing-Based Desktop Application

<p align="center">
  <strong>Object-Oriented Programming | Java Swing | File Handling</strong>
</p>

---

## Overview

The **Student Management System** is a desktop-based application developed using Java and Java Swing to simplify and organize student record management.

The application provides an intuitive graphical user interface that enables administrators to perform essential student management operations, including registration, viewing, updating, deleting, and searching student records.

Developed with a strong emphasis on Object-Oriented Programming (OOP) principles, the project demonstrates the practical implementation of abstraction, encapsulation, inheritance, and polymorphism.

Student information is stored locally using Java file handling, ensuring data persistence without requiring an external database.

---

## Key Features

| Module | Description |
|---|---|
| Dashboard | Displays student statistics and registration summaries. |
| Student Registration | Adds new students with validated information. |
| Student Records | Displays all registered students in an interactive table. |
| Update Records | Modifies existing student information. |
| Delete Records | Removes student records with confirmation. |
| Search System | Searches students using multiple criteria. |
| Analytics | Displays department-wise and gender-wise statistics. |
| Data Persistence | Automatically saves and retrieves student information. |

### Additional Functionalities

- Automatic Student ID generation.
- Duplicate ID and Roll Number prevention.
- Email and mobile number validation.
- Department and semester filtering.
- Interactive tables with modern styling.
- Confirmation dialogs for important operations.
- Automatic synchronization with local CSV storage.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Graphical User Interface |
| Java AWT | GUI layouts and components |
| Java File Handling | Local data persistence |
| CSV | Student record storage |
| Git | Version control |
| GitHub | Source code hosting |

**Development Environment:** VS Code, IntelliJ IDEA, Eclipse, or Apache NetBeans.

**Java Compatibility:** JDK 8 or above.

---

## Object-Oriented Programming Implementation

The application incorporates the four fundamental principles of OOP.

### 1. Encapsulation
- Data members are declared private.
- Public getter and setter methods control access.
- Input validation is implemented to maintain data integrity.

### 2. Inheritance
- The `Student` class extends the abstract `Person` class.
- Common personal attributes and behaviours are inherited.
- Constructor chaining is implemented using the `super()` keyword.

### 3. Polymorphism
- Method overriding is implemented using the `@Override` annotation.
- Method overloading is used in search and update operations.
- Interface-based polymorphism is demonstrated through `RecordOperations`.

### 4. Abstraction
- The abstract `Person` class defines common attributes and abstract methods.
- The `RecordOperations` interface defines standard CRUD operations.
- Implementation details are separated from application functionality.

### Other Java Concepts
- Classes and Objects
- Constructors
- Interfaces
- Abstract Classes
- Exception Handling
- Collections Framework
- File I/O Operations

---

## Project Architecture

The project follows a modular structure that separates the user interface, business logic, data management, and model classes.

```text
MiniProject/
│
├── bin/
│
├── data/
│   └── students.csv
│
├── src/
│   ├── Main.java
│   │
│   ├── model/
│   │   ├── Person.java
│   │   ├── Student.java
│   │   └── ValidationException.java
│   │
│   ├── dao/
│   │   ├── RecordOperations.java
│   │   └── FileHandler.java
│   │
│   ├── service/
│   │   └── StudentManager.java
│   │
│   ├── ui/
│   │   ├── UITheme.java
│   │   ├── ModernButton.java
│   │   ├── ModernTable.java
│   │   └── StudentGUI.java
│   │
│   └── test/
│       └── SystemVerifier.java
│
├── compile_and_run.bat
├── compile_and_run.sh
├── README.md
└── .gitignore
```

### Package Description

| Package | Responsibility |
|---|---|
| `model` | Defines student entities and validation exceptions. |
| `dao` | Handles CRUD interfaces and file operations. |
| `service` | Implements business logic and student management. |
| `ui` | Contains GUI components and interface design. |
| `test` | Contains automated application verification. |

---

## Application Interface

The application features a modern desktop interface designed with a professional colour palette.

**Design Elements:**
- Dark navy blue navigation sidebar.
- Clean white and light-blue backgrounds.
- Interactive dashboard cards.
- Custom rounded buttons.
- Structured student data tables.
- Responsive layouts.
- Consistent typography and spacing.

---

## Installation and Execution

### Prerequisites

Ensure that the following software is installed:

- Java Development Kit (JDK 8 or above).
- A Java-compatible IDE or terminal.
- Git (optional).

Verify your Java installation:

```bash
java -version
javac -version
```

### Option 1: Windows

1. Open the `MiniProject` folder.
2. Double-click `compile_and_run.bat`.
3. Wait for compilation to complete.
4. The application will launch automatically.

Alternatively, execute:

```bash
compile_and_run.bat
```

### Option 2: Linux / macOS

Open the terminal inside the project directory.

Execute:

```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

### Option 3: Manual Compilation

Create the output directory:

```bash
mkdir bin
```

Compile the application:

```bash
javac -encoding UTF-8 -d bin src/model/*.java src/dao/*.java src/service/*.java src/ui/*.java src/Main.java
```

Run the application:

```bash
java -cp bin Main
```

---

## Data Management

The application uses local CSV-based file storage.

**Storage Location:**

```text
data/students.csv
```

### Data Management Capabilities

- Automatic loading of previously saved student records.
- Persistent storage of newly registered students.
- Automatic synchronization after updating records.
- Permanent removal of deleted records.
- No external database configuration required.

---

## Testing and Verification

The project includes an automated verification utility:

`SystemVerifier.java`

The verification covers the following functionalities:

- Inheritance and abstraction.
- Encapsulation and input validation.
- CRUD operations.
- Duplicate student ID prevention.
- Duplicate roll number prevention.
- Method overloading.
- Student record updates.
- File persistence and retrieval.
- Student record deletion.
- Department and gender analytics.

Run the verification using:

```bash
javac -encoding UTF-8 -d bin -cp bin src/test/SystemVerifier.java
java -ea -cp bin test.SystemVerifier
```

---

## Project Objectives

1. To understand the practical implementation of Object-Oriented Programming concepts.
2. To develop a desktop application using Java Swing.
3. To implement CRUD operations for student record management.
4. To understand Java file handling and data persistence.
5. To develop a structured and user-friendly graphical interface.
6. To improve problem-solving and software development skills.

---

## Academic Information

| Field | Details |
|---|---|
| Project Title | Student Management System |
| Subject | Object-Oriented Programming |
| Activity | CIE-2 Activity-6 – OOP GUI Application |
| Academic Level | S.Y. B.Tech |
| Academic Year | 2026–2027 |
| Project Type | Desktop GUI Application |

---

## Future Enhancements

Potential improvements for future versions include:

- Database integration using MySQL.
- User authentication and role-based access.
- PDF report generation.
- Student profile image management.
- Export student records to Excel.
- Advanced analytics and reporting.

---

## Author

**Developed as an academic project for the Second Year B.Tech Object-Oriented Programming curriculum.**

---

## License

This project is intended for educational and academic purposes.

---

<p align="center">
  <strong>Student Management System</strong><br>
  Java • Swing • OOP • File Handling
</p>
