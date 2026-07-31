-- ==================================================================
-- Student Grade Tracker - Database Setup Script
-- Run this entire script in MySQL Workbench (or via mysql CLI)
-- ==================================================================

-- 1. Create the database
DROP DATABASE IF EXISTS student_grade_tracker;
CREATE DATABASE student_grade_tracker;
USE student_grade_tracker;

-- 2. Create the students table
CREATE TABLE students (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    roll_number    VARCHAR(20) UNIQUE NOT NULL,
    student_name   VARCHAR(100) NOT NULL,
    course         VARCHAR(100) NOT NULL,
    subject1       INT NOT NULL CHECK (subject1 BETWEEN 0 AND 100),
    subject2       INT NOT NULL CHECK (subject2 BETWEEN 0 AND 100),
    subject3       INT NOT NULL CHECK (subject3 BETWEEN 0 AND 100),
    subject4       INT NOT NULL CHECK (subject4 BETWEEN 0 AND 100),
    subject5       INT NOT NULL CHECK (subject5 BETWEEN 0 AND 100),
    total          INT NOT NULL,
    average_marks  DOUBLE NOT NULL,
    highest_marks  INT NOT NULL,
    lowest_marks   INT NOT NULL,
    percentage     DOUBLE NOT NULL,
    grade          VARCHAR(5) NOT NULL
);

-- 3. Insert sample records
-- (total, average_marks, highest_marks, lowest_marks, percentage, and grade
--  are pre-calculated here to match what the Java application would compute)

INSERT INTO students
    (roll_number, student_name, course, subject1, subject2, subject3, subject4, subject5,
     total, average_marks, highest_marks, lowest_marks, percentage, grade)
VALUES
    ('R001', 'Aditi Sharma',   'Computer Science', 95, 92, 88, 90, 96, 461, 92.2, 96, 88, 92.2,  'A+'),
    ('R002', 'Rohan Verma',    'Information Tech',  78, 82, 75, 80, 79, 394, 78.8, 82, 75, 78.8,  'B'),
    ('R003', 'Simran Kaur',    'Electronics',       65, 70, 60, 68, 72, 335, 67.0, 72, 60, 67.0,  'C'),
    ('R004', 'Karan Mehta',    'Mechanical',        45, 55, 48, 50, 52, 250, 50.0, 55, 45, 50.0,  'D'),
    ('R005', 'Neha Gupta',     'Computer Science',  30, 40, 35, 28, 45, 178, 35.6, 45, 28, 35.6,  'F'),
    ('R006', 'Arjun Singh',    'Civil Engineering', 88, 85, 91, 84, 89, 437, 87.4, 91, 84, 87.4,  'A'),
    ('R007', 'Priya Nair',     'Information Tech',  99, 97, 95, 98, 96, 485, 97.0, 99, 95, 97.0,  'A+'),
    ('R008', 'Aman Yadav',     'Electronics',       58, 62, 55, 60, 59, 294, 58.8, 62, 55, 58.8,  'D');

-- 4. Quick verification query
SELECT * FROM students;
