package Models;

import java.util.*;

public class Student {
    private final String name;
    private final int age;
    private final double averageGrade;
    private final String faculty;
    private final int yearOfStudy;

    public Student(String name, int age, double averageGrade, String faculty, int yearOfStudy) {
        this.name = Objects.requireNonNull(name);
        if (age <= 0) throw new IllegalArgumentException("age must be > 0");
        if (averageGrade < 0 || averageGrade > 5) throw new IllegalArgumentException("avg must be 0..5");
        this.age = age;
        this.averageGrade = averageGrade;
        this.faculty = Objects.requireNonNull(faculty);
        this.yearOfStudy = yearOfStudy;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getAverageGrade() { return averageGrade; }
    public String getFaculty() { return faculty; }
    public int getYearOfStudy() { return yearOfStudy; }

    @Override public String toString() {
        return "%s, %s, y%d, age %d, avg %.2f".formatted(name, faculty, yearOfStudy, age, averageGrade);
    }

    // Рівність: у навчальних прикладах можна вважати ім'я+факультет+рік як ідентифікатор
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student s)) return false;
        return age == s.age &&
                Double.compare(s.averageGrade, averageGrade) == 0 &&
                yearOfStudy == s.yearOfStudy &&
                name.equals(s.name) &&
                faculty.equals(s.faculty);
    }
    @Override public int hashCode() {
        return Objects.hash(name, age, averageGrade, faculty, yearOfStudy);
    }
}
