import Models.*;
import Comparators.*;
import Utils.*;
import java.util.*;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Student> students = List.of(
                new Student("Olena", 19, 4.90, "CS", 1),
                new Student("Maksym", 21, 4.30, "Math", 3),
                new Student("Iryna", 20, 4.70, "CS", 2),
                new Student("Taras", 22, 4.10, "Physics", 3),
                new Student("Sofiia", 18, 4.95, "Design", 1),
                new Student("Andrii", 23, 4.40, "Math", 4),
                new Student("Nazar", 21, 4.80, "CS", 3),
                new Student("Yuliia", 20, 4.55, "Economics", 2),
                new Student("Bohdan", 19, 3.95, "CS", 1),
                new Student("Kateryna", 22, 4.75, "Design", 4),
                new Student("Petro", 20, 4.25, "Physics", 2)
        );

        System.out.println("Sorted by rating");
        StudentUtils.sort(students, new StudentRatingComparator()).forEach(System.out::println);

        System.out.println("\nCS honors (avg > 4.5)");
        Predicate<Student> csHonors = StudentFilter.byFaculty("CS").and(StudentFilter.byAvgRange(4.5, 5.0));
        StudentUtils.filter(students, csHonors).forEach(System.out::println);

        System.out.println("\nYear 3 sorted by age");
        StudentUtils.filterAndSort(students, StudentFilter.byYear(3), new ComparatorByAgeAsc())
                .forEach(System.out::println);

        System.out.println("\nTop-5 by average grade");
        StudentUtils.topN(students, new ComparatorByAgeAsc(), 5).forEach(System.out::println);

        System.out.println("\nCustom filtration and sorting chain");
        Predicate<Student> designOrEcon = s -> Set.of("Design", "Economics").contains(s.getFaculty());
        Predicate<Student> age18to21 = StudentFilter.byAgeRange(18, 21);
        StudentUtils.filterAndSort(students, designOrEcon.and(age18to21), new StudentPerformanceComparator())
                .forEach(System.out::println);
    }
}