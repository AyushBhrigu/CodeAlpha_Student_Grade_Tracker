package util;

/**
 * GradeCalculator.java
 *
 * Pure utility class (no state) that performs all the numeric
 * calculations needed for a student's result: total, average,
 * highest, lowest, percentage, and letter grade.
 *
 * Keeping this logic separate from Student/StudentService follows
 * the Single Responsibility Principle and avoids duplicate code.
 */
public class GradeCalculator {

    private static final int NUMBER_OF_SUBJECTS = 5;
    private static final int MAX_MARKS_PER_SUBJECT = 100;

    private GradeCalculator() {
        // Utility class - prevent instantiation
    }

    public static int calculateTotal(int[] marks) {
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        return total;
    }

    public static double calculateAverage(int[] marks) {
        return (double) calculateTotal(marks) / NUMBER_OF_SUBJECTS;
    }

    public static int calculateHighest(int[] marks) {
        int highest = marks[0];
        for (int mark : marks) {
            if (mark > highest) {
                highest = mark;
            }
        }
        return highest;
    }

    public static int calculateLowest(int[] marks) {
        int lowest = marks[0];
        for (int mark : marks) {
            if (mark < lowest) {
                lowest = mark;
            }
        }
        return lowest;
    }

    public static double calculatePercentage(int[] marks) {
        int total = calculateTotal(marks);
        int maxTotal = NUMBER_OF_SUBJECTS * MAX_MARKS_PER_SUBJECT;
        return ((double) total / maxTotal) * 100;
    }

    /**
     * Determines the letter grade based on percentage using the
     * following criteria:
     *   A+ : 90-100
     *   A  : 80-89
     *   B  : 70-79
     *   C  : 60-69
     *   D  : 50-59
     *   F  : Below 50
     */
    public static String calculateGrade(double percentage) {
        if (percentage >= 90) {
            return "A+";
        } else if (percentage >= 80) {
            return "A";
        } else if (percentage >= 70) {
            return "B";
        } else if (percentage >= 60) {
            return "C";
        } else if (percentage >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    /**
     * A student passes if their percentage is 50 or above (i.e. grade is not F).
     */
    public static boolean isPass(double percentage) {
        return percentage >= 50;
    }
}
