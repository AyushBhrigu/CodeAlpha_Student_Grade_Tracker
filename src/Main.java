import model.Student;
import service.StudentService;
import util.GradeCalculator;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java
 *
 * Entry point and presentation layer for the Student Grade Tracker
 * console application. Handles the menu loop, user input, input
 * validation, and formatted output. All persistence logic is
 * delegated to StudentService.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("Connecting to database...");
        try {
            database.DBConnection.getConnection();
            System.out.println("Connected successfully!\n");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database.");
            System.out.println("Details: " + e.getMessage());
            System.out.println("Please check DBConnection.java credentials and that MySQL is running.");
            return; // Cannot proceed without a database connection
        }

        while (running) {
            printMenu();
            int choice = readIntSafely("Enter Choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    calculateResult();
                    break;
                case 7:
                    summaryReport();
                    break;
                case 8:
                    running = false;
                    System.out.println("Exiting Student Grade Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
            }
        }

        database.DBConnection.closeConnection();
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=====================================");
        System.out.println("      STUDENT GRADE TRACKER");
        System.out.println("=====================================");
        System.out.println("1. Add Student");
        System.out.println("2. View Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Calculate Result");
        System.out.println("7. Summary Report");
        System.out.println("8. Exit");
        System.out.println("=====================================");
    }

    // ------------------------------------------------------------------
    // 1. ADD STUDENT
    // ------------------------------------------------------------------
    private static void addStudent() {
        System.out.println("\n--- Add Student ---");
        try {
            String rollNumber = readNonEmptyString("Enter Roll Number: ");

            if (studentService.rollNumberExists(rollNumber)) {
                System.out.println("ERROR: Roll number '" + rollNumber + "' already exists. Please use a unique roll number.");
                return;
            }

            String name = readNonEmptyString("Enter Student Name: ");
            String course = readNonEmptyString("Enter Course: ");

            int subject1 = readMarks("Enter Subject 1 Marks (0-100): ");
            int subject2 = readMarks("Enter Subject 2 Marks (0-100): ");
            int subject3 = readMarks("Enter Subject 3 Marks (0-100): ");
            int subject4 = readMarks("Enter Subject 4 Marks (0-100): ");
            int subject5 = readMarks("Enter Subject 5 Marks (0-100): ");

            Student student = new Student(rollNumber, name, course,
                    subject1, subject2, subject3, subject4, subject5);

            studentService.addStudent(student);
            System.out.println("Student added successfully!");

        } catch (SQLException e) {
            System.out.println("Database error while adding student: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // 2. VIEW ALL STUDENTS
    // ------------------------------------------------------------------
    private static void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        try {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("No student records found.");
                return;
            }
            printStudentTable(students);
        } catch (SQLException e) {
            System.out.println("Database error while fetching students: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // 3. SEARCH STUDENT
    // ------------------------------------------------------------------
    private static void searchStudent() {
        System.out.println("\n--- Search Student ---");
        System.out.println("1. Search by Roll Number");
        System.out.println("2. Search by Name");
        int choice = readIntSafely("Enter Choice: ");

        try {
            if (choice == 1) {
                String rollNumber = readNonEmptyString("Enter Roll Number: ");
                Student student = studentService.searchByRollNumber(rollNumber);
                if (student == null) {
                    System.out.println("No student found with roll number: " + rollNumber);
                } else {
                    printStudentTable(List.of(student));
                }
            } else if (choice == 2) {
                String name = readNonEmptyString("Enter Student Name (or part of it): ");
                List<Student> results = studentService.searchByName(name);
                if (results.isEmpty()) {
                    System.out.println("No students found matching name: " + name);
                } else {
                    printStudentTable(results);
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (SQLException e) {
            System.out.println("Database error while searching: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // 4. UPDATE STUDENT
    // ------------------------------------------------------------------
    private static void updateStudent() {
        System.out.println("\n--- Update Student ---");
        try {
            String rollNumber = readNonEmptyString("Enter Roll Number of student to update: ");
            Student existing = studentService.searchByRollNumber(rollNumber);

            if (existing == null) {
                System.out.println("No student found with roll number: " + rollNumber);
                return;
            }

            System.out.println("Current record:");
            printStudentTable(List.of(existing));
            System.out.println("Enter new marks (results will be recalculated automatically):");

            int subject1 = readMarks("Enter Subject 1 Marks (0-100): ");
            int subject2 = readMarks("Enter Subject 2 Marks (0-100): ");
            int subject3 = readMarks("Enter Subject 3 Marks (0-100): ");
            int subject4 = readMarks("Enter Subject 4 Marks (0-100): ");
            int subject5 = readMarks("Enter Subject 5 Marks (0-100): ");

            boolean updated = studentService.updateStudent(rollNumber, subject1, subject2, subject3, subject4, subject5);
            if (updated) {
                System.out.println("Student record updated successfully!");
            } else {
                System.out.println("Update failed. No matching record was modified.");
            }
        } catch (SQLException e) {
            System.out.println("Database error while updating student: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // 5. DELETE STUDENT
    // ------------------------------------------------------------------
    private static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        try {
            String rollNumber = readNonEmptyString("Enter Roll Number of student to delete: ");
            Student existing = studentService.searchByRollNumber(rollNumber);

            if (existing == null) {
                System.out.println("No student found with roll number: " + rollNumber);
                return;
            }

            System.out.print("Are you sure you want to delete this record? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes") || confirm.equals("y")) {
                boolean deleted = studentService.deleteStudent(rollNumber);
                System.out.println(deleted ? "Student deleted successfully!" : "Delete failed.");
            } else {
                System.out.println("Delete cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("Database error while deleting student: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // 6. CALCULATE RESULT
    // ------------------------------------------------------------------
    private static void calculateResult() {
        System.out.println("\n--- Calculate Student Result ---");
        try {
            String rollNumber = readNonEmptyString("Enter Roll Number: ");
            Student student = studentService.searchByRollNumber(rollNumber);

            if (student == null) {
                System.out.println("No student found with roll number: " + rollNumber);
                return;
            }

            System.out.println("\nResult for " + student.getStudentName() + " (Roll No: " + student.getRollNumber() + ")");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-20s: %d%n", "Total Marks", student.getTotal());
            System.out.printf("%-20s: %.2f%n", "Average Marks", student.getAverageMarks());
            System.out.printf("%-20s: %d%n", "Highest Marks", student.getHighestMarks());
            System.out.printf("%-20s: %d%n", "Lowest Marks", student.getLowestMarks());
            System.out.printf("%-20s: %.2f%%%n", "Percentage", student.getPercentage());
            System.out.printf("%-20s: %s%n", "Grade", student.getGrade());
            System.out.println("-------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Database error while calculating result: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // 7. SUMMARY REPORT
    // ------------------------------------------------------------------
    private static void summaryReport() {
        System.out.println("\n--- Summary Report ---");
        try {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("No student records found. Cannot generate summary.");
                return;
            }

            int totalStudents = students.size();
            double classTotalPercentage = 0;
            int passCount = 0;
            int failCount = 0;

            int gradeAPlus = 0, gradeA = 0, gradeB = 0, gradeC = 0, gradeD = 0, gradeF = 0;

            Student highestScorer = students.get(0);
            Student lowestScorer = students.get(0);

            for (Student s : students) {
                classTotalPercentage += s.getPercentage();

                if (s.getTotal() > highestScorer.getTotal()) {
                    highestScorer = s;
                }
                if (s.getTotal() < lowestScorer.getTotal()) {
                    lowestScorer = s;
                }

                if (GradeCalculator.isPass(s.getPercentage())) {
                    passCount++;
                } else {
                    failCount++;
                }

                switch (s.getGrade()) {
                    case "A+": gradeAPlus++; break;
                    case "A": gradeA++; break;
                    case "B": gradeB++; break;
                    case "C": gradeC++; break;
                    case "D": gradeD++; break;
                    default: gradeF++; break;
                }
            }

            double classAverage = classTotalPercentage / totalStudents;

            System.out.println("=====================================");
            System.out.println("           SUMMARY REPORT");
            System.out.println("=====================================");
            System.out.println("Total Students   : " + totalStudents);
            System.out.println("Highest Scorer   : " + highestScorer.getStudentName()
                    + " (Roll No: " + highestScorer.getRollNumber() + ", Total: " + highestScorer.getTotal() + ")");
            System.out.println("Lowest Scorer    : " + lowestScorer.getStudentName()
                    + " (Roll No: " + lowestScorer.getRollNumber() + ", Total: " + lowestScorer.getTotal() + ")");
            System.out.printf("Class Average    : %.2f%%%n", classAverage);
            System.out.println("-------------------------------------");
            System.out.println("Grade Distribution:");
            System.out.println("  A+ : " + gradeAPlus);
            System.out.println("  A  : " + gradeA);
            System.out.println("  B  : " + gradeB);
            System.out.println("  C  : " + gradeC);
            System.out.println("  D  : " + gradeD);
            System.out.println("  F  : " + gradeF);
            System.out.println("-------------------------------------");
            System.out.println("Pass Count       : " + passCount);
            System.out.println("Fail Count       : " + failCount);
            System.out.println("=====================================");

        } catch (SQLException e) {
            System.out.println("Database error while generating summary report: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Helper: formatted table printing
    // ------------------------------------------------------------------
    private static void printStudentTable(List<Student> students) {
        String format = "%-5s %-12s %-20s %-12s %-6s %-6s %-6s %-6s %-6s %-6s %-8s %-6s%n";
        System.out.printf(format, "ID", "Roll No", "Name", "Course", "S1", "S2", "S3", "S4", "S5", "Total", "Percent", "Grade");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf(format,
                    s.getId(),
                    s.getRollNumber(),
                    s.getStudentName(),
                    s.getCourse(),
                    s.getSubject1(),
                    s.getSubject2(),
                    s.getSubject3(),
                    s.getSubject4(),
                    s.getSubject5(),
                    s.getTotal(),
                    String.format("%.2f", s.getPercentage()),
                    s.getGrade());
        }
    }

    // ------------------------------------------------------------------
    // Input helpers with validation
    // ------------------------------------------------------------------

    /** Reads an integer safely, re-prompting on invalid (non-numeric) input. */
    private static int readIntSafely(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /** Reads a non-empty string, re-prompting until valid input is given. */
    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            } else {
                return input;
            }
        }
    }

    /** Reads marks and validates that they are between 0 and 100 inclusive. */
    private static int readMarks(String prompt) {
        while (true) {
            int marks = readIntSafely(prompt);
            if (marks < 0 || marks > 100) {
                System.out.println("Marks must be between 0 and 100. Please try again.");
            } else {
                return marks;
            }
        }
    }
}
