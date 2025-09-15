package Utils;

import Models.*;
import java.util.*;
import java.util.function.Predicate;

public class StudentFilter {
    public static Predicate<Student> byFaculty(String faculty) {
        return s -> s.getFaculty().equalsIgnoreCase(faculty);
    }
    public static Predicate<Student> byAvgRange(double min, double max) {
        return s -> s.getAverageGrade() >= min && s.getAverageGrade() <= max;
    }
    public static Predicate<Student> byYear(int year) {
        return s -> s.getYearOfStudy() == year;
    }
    public static Predicate<Student> byAgeRange(int min, int max) {
        return s -> s.getAge() >= min && s.getAge() <= max;
    }
}