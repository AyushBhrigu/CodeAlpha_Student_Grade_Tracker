package service;

import database.DBConnection;
import model.Student;
import util.GradeCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentService.java
 *
 * Contains all the business logic and JDBC CRUD operations for
 * managing students in the database. Main.java (the presentation
 * layer) calls into this service and never talks to JDBC directly.
 */
public class StudentService {

    /**
     * Checks whether a roll number already exists in the database.
     * Used to enforce the "Roll Number must be unique" requirement.
     */
    public boolean rollNumberExists(String rollNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Inserts a new student record, computing all derived fields
     * (total, average, highest, lowest, percentage, grade) before saving.
     */
    public void addStudent(Student student) throws SQLException {
        int[] marks = student.getAllMarks();
        populateCalculatedFields(student, marks);

        String sql = "INSERT INTO students (roll_number, student_name, course, " +
                "subject1, subject2, subject3, subject4, subject5, " +
                "total, average_marks, highest_marks, lowest_marks, percentage, grade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getRollNumber());
            ps.setString(2, student.getStudentName());
            ps.setString(3, student.getCourse());
            ps.setInt(4, student.getSubject1());
            ps.setInt(5, student.getSubject2());
            ps.setInt(6, student.getSubject3());
            ps.setInt(7, student.getSubject4());
            ps.setInt(8, student.getSubject5());
            ps.setInt(9, student.getTotal());
            ps.setDouble(10, student.getAverageMarks());
            ps.setInt(11, student.getHighestMarks());
            ps.setInt(12, student.getLowestMarks());
            ps.setDouble(13, student.getPercentage());
            ps.setString(14, student.getGrade());
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves every student record from the database, ordered by id.
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }

    /**
     * Searches for a single student by their unique roll number.
     * Returns null if no match is found.
     */
    public Student searchByRollNumber(String rollNumber) throws SQLException {
        String sql = "SELECT * FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        return null;
    }

    /**
     * Searches for students whose name contains the given search term
     * (case-insensitive, partial match).
     */
    public List<Student> searchByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE LOWER(student_name) LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        }
        return students;
    }

    /**
     * Updates a student's marks (and course, if desired) and automatically
     * recalculates all derived result fields before saving.
     */
    public boolean updateStudent(String rollNumber, int subject1, int subject2,
                                  int subject3, int subject4, int subject5) throws SQLException {
        int[] marks = { subject1, subject2, subject3, subject4, subject5 };
        int total = GradeCalculator.calculateTotal(marks);
        double average = GradeCalculator.calculateAverage(marks);
        int highest = GradeCalculator.calculateHighest(marks);
        int lowest = GradeCalculator.calculateLowest(marks);
        double percentage = GradeCalculator.calculatePercentage(marks);
        String grade = GradeCalculator.calculateGrade(percentage);

        String sql = "UPDATE students SET subject1 = ?, subject2 = ?, subject3 = ?, " +
                "subject4 = ?, subject5 = ?, total = ?, average_marks = ?, " +
                "highest_marks = ?, lowest_marks = ?, percentage = ?, grade = ? " +
                "WHERE roll_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subject1);
            ps.setInt(2, subject2);
            ps.setInt(3, subject3);
            ps.setInt(4, subject4);
            ps.setInt(5, subject5);
            ps.setInt(6, total);
            ps.setDouble(7, average);
            ps.setInt(8, highest);
            ps.setInt(9, lowest);
            ps.setDouble(10, percentage);
            ps.setString(11, grade);
            ps.setString(12, rollNumber);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Deletes a student record identified by roll number.
     * Returns true if a record was actually deleted.
     */
    public boolean deleteStudent(String rollNumber) throws SQLException {
        String sql = "DELETE FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNumber);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Computes total/average/highest/lowest/percentage/grade for a student
     * object in memory and sets those fields on the object.
     */
    private void populateCalculatedFields(Student student, int[] marks) {
        student.setTotal(GradeCalculator.calculateTotal(marks));
        student.setAverageMarks(GradeCalculator.calculateAverage(marks));
        student.setHighestMarks(GradeCalculator.calculateHighest(marks));
        student.setLowestMarks(GradeCalculator.calculateLowest(marks));
        double percentage = GradeCalculator.calculatePercentage(marks);
        student.setPercentage(percentage);
        student.setGrade(GradeCalculator.calculateGrade(percentage));
    }

    /**
     * Maps the current row of a ResultSet to a Student object.
     * Centralizing this avoids duplicating column-mapping code across
     * getAllStudents / searchByRollNumber / searchByName.
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("roll_number"),
                rs.getString("student_name"),
                rs.getString("course"),
                rs.getInt("subject1"),
                rs.getInt("subject2"),
                rs.getInt("subject3"),
                rs.getInt("subject4"),
                rs.getInt("subject5"),
                rs.getInt("total"),
                rs.getDouble("average_marks"),
                rs.getInt("highest_marks"),
                rs.getInt("lowest_marks"),
                rs.getDouble("percentage"),
                rs.getString("grade")
        );
    }
}
