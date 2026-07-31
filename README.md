# Student Grade Tracker

A console-based Java application for managing student academic records — adding, searching, updating, and deleting students, calculating results, and generating class-wide summary reports. Data is persisted in a MySQL database via JDBC.

Built entirely with **Core Java** (no frameworks, no build tools) to keep the codebase simple, transparent, and easy to learn from.

---

## Features

1. **Add Student** — enter roll number, name, course, and marks for 5 subjects, with full input validation (unique roll number, marks between 0–100, non-empty names).
2. **View Students** — display every student record in a neatly formatted table.
3. **Search Student** — look up a student by roll number or by name (partial match supported).
4. **Update Student** — update a student's marks; total, average, percentage, and grade are recalculated automatically.
5. **Delete Student** — remove a student record (with confirmation prompt).
6. **Calculate Result** — view total marks, average, highest, lowest, percentage, and letter grade for any student.
7. **Summary Report** — class-wide statistics: total students, highest/lowest scorer, class average, grade distribution, pass/fail counts.
8. **Exit** — cleanly closes the database connection and ends the program.

Grade scale:

| Percentage | Grade |
|------------|-------|
| 90–100     | A+    |
| 80–89      | A     |
| 70–79      | B     |
| 60–69      | C     |
| 50–59      | D     |
| Below 50   | F     |

---

## Technologies Used

- **Java 21** (Core Java only — no Spring, no Hibernate, no JPA)
- **JDBC** for database connectivity
- **MySQL** for data storage
- **MySQL Workbench** for database management
- **VS Code** as the development environment
- **Git & GitHub** for version control

No external libraries or frameworks are used, apart from the MySQL JDBC driver, which is required to let core Java's JDBC API talk to a MySQL server.

---

## Folder Structure

```
StudentGradeTracker/
│
├── src/
│   ├── model/
│   │      Student.java            # Student entity (POJO)
│   │
│   ├── database/
│   │      DBConnection.java       # JDBC connection manager
│   │
│   ├── service/
│   │      StudentService.java     # Business logic + CRUD operations
│   │
│   ├── util/
│   │      GradeCalculator.java    # Grade/statistics calculations
│   │
│   └── Main.java                  # Console menu / entry point
│
├── sql/
│      student_grade_tracker.sql   # Database + table creation + sample data
│
└── README.md
```

---

## Database Setup

1. Open **MySQL Workbench** and connect to your local MySQL server.
2. Open the file `sql/student_grade_tracker.sql`.
3. Execute the entire script (it will drop and recreate the `student_grade_tracker` database, create the `students` table, and insert sample records).
4. Verify the setup with:
   ```sql
   USE student_grade_tracker;
   SELECT * FROM students;
   ```

You should see 8 sample student records.

---

## How to Run

### 1. Get the MySQL JDBC Driver

Download the **MySQL Connector/J** jar (e.g. `mysql-connector-j-9.0.0.jar`) from the [official MySQL site](https://dev.mysql.com/downloads/connector/j/) and place it inside the project, e.g. in a `lib/` folder:

```
StudentGradeTracker/lib/mysql-connector-j-9.0.0.jar
```

### 2. Update Database Credentials

Open `src/database/DBConnection.java` and update these constants to match your MySQL setup:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grade_tracker";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password";
```

### 3. Compile the Project (VS Code Terminal)

From inside the `StudentGradeTracker` folder, run:

```bash
javac -d bin -cp "lib/mysql-connector-j-9.0.0.jar" src/model/*.java src/database/*.java src/service/*.java src/util/*.java src/Main.java
```

This compiles all source files into a `bin/` output folder while preserving the package structure.

### 4. Run the Project

**On macOS/Linux:**
```bash
java -cp "bin:lib/mysql-connector-j-9.0.0.jar" Main
```

**On Windows:**
```bash
java -cp "bin;lib/mysql-connector-j-9.0.0.jar" Main
```

### 5. Using VS Code Directly

If you're using the VS Code **Extension Pack for Java**:
1. Open the `StudentGradeTracker` folder in VS Code.
2. Add the MySQL Connector/J jar to your classpath (VS Code will usually prompt you, or add it via `Java Projects` panel → `Referenced Libraries` → `Add Jar`).
3. Open `src/Main.java` and click **Run** (or press `F5`).

---

## Sample Menu

```
=====================================
      STUDENT GRADE TRACKER
=====================================
1. Add Student
2. View Students
3. Search Student
4. Update Student
5. Delete Student
6. Calculate Result
7. Summary Report
8. Exit
=====================================
Enter Choice:
```

---

## Screenshots

*(Add screenshots of the running application here)*

- `screenshots/Main Menu.png` — Main menu
- `screenshots/Add Student.png` — Adding a student
- `screenshots/View Students.png` — Viewing all students
- `screenshots/Search Student 1.png` — Viewing all students
- `screenshots/Search Student 2.png` — Viewing all students
- `screenshots/Update Student.png` — Viewing all students
- `screenshots/Delete Student.png` — Viewing all students
- `screenshots/Calculate Result.png` — Viewing all students
- `screenshots/Summary Report.png` — Summary report output
- `screenshots/Exit.png` — Viewing all students

---

## Future Enhancements

- Export student reports to CSV or PDF.
- Add attendance tracking alongside grades.
- Support multiple courses/semesters per student.
- Add a simple login system for teachers/admins.
- Add unit tests for `GradeCalculator` and `StudentService`.
- Optional: build a lightweight GUI (JavaFX) or REST API layer on top of the existing service layer, without altering the core logic.

---

## License

This project is free to use and modify for learning purposes.
