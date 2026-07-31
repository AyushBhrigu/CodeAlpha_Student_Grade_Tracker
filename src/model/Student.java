package model;

/**
 * Student.java
 *
 * Model class representing a Student entity.
 * Follows encapsulation - all fields are private and accessed via getters/setters.
 */
public class Student {

    private int id;                 // Auto-generated primary key from DB
    private String rollNumber;      // Unique roll number
    private String studentName;
    private String course;
    private int subject1;
    private int subject2;
    private int subject3;
    private int subject4;
    private int subject5;

    // Derived / calculated fields
    private int total;
    private double averageMarks;
    private int highestMarks;
    private int lowestMarks;
    private double percentage;
    private String grade;

    // Default constructor
    public Student() {
    }

    // Constructor for creating a new student (before DB insert, no id yet)
    public Student(String rollNumber, String studentName, String course,
                   int subject1, int subject2, int subject3, int subject4, int subject5) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.course = course;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
    }

    // Full constructor (used when loading a record back from the database)
    public Student(int id, String rollNumber, String studentName, String course,
                   int subject1, int subject2, int subject3, int subject4, int subject5,
                   int total, double averageMarks, int highestMarks, int lowestMarks,
                   double percentage, String grade) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.course = course;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
        this.total = total;
        this.averageMarks = averageMarks;
        this.highestMarks = highestMarks;
        this.lowestMarks = lowestMarks;
        this.percentage = percentage;
        this.grade = grade;
    }

    // ------------------- Getters and Setters -------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSubject1() {
        return subject1;
    }

    public void setSubject1(int subject1) {
        this.subject1 = subject1;
    }

    public int getSubject2() {
        return subject2;
    }

    public void setSubject2(int subject2) {
        this.subject2 = subject2;
    }

    public int getSubject3() {
        return subject3;
    }

    public void setSubject3(int subject3) {
        this.subject3 = subject3;
    }

    public int getSubject4() {
        return subject4;
    }

    public void setSubject4(int subject4) {
        this.subject4 = subject4;
    }

    public int getSubject5() {
        return subject5;
    }

    public void setSubject5(int subject5) {
        this.subject5 = subject5;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public int getHighestMarks() {
        return highestMarks;
    }

    public void setHighestMarks(int highestMarks) {
        this.highestMarks = highestMarks;
    }

    public int getLowestMarks() {
        return lowestMarks;
    }

    public void setLowestMarks(int lowestMarks) {
        this.lowestMarks = lowestMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Returns an array of the five subject marks.
     * Useful for GradeCalculator to compute statistics without repeating code.
     */
    public int[] getAllMarks() {
        return new int[] { subject1, subject2, subject3, subject4, subject5 };
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", rollNumber='" + rollNumber + '\'' +
                ", studentName='" + studentName + '\'' +
                ", course='" + course + '\'' +
                ", total=" + total +
                ", percentage=" + percentage +
                ", grade='" + grade + '\'' +
                '}';
    }
}
