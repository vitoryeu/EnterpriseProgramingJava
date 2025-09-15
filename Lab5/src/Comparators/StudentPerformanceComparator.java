package Comparators;

import Models.*;
import java.util.*;

public class StudentPerformanceComparator implements Comparator<Student> {
    @Override public int compare(Student a, Student b) {
        int byAvg = -Double.compare(a.getAverageGrade(), b.getAverageGrade());
        if (byAvg != 0) return byAvg;
        int byYear = -Integer.compare(a.getYearOfStudy(), b.getYearOfStudy());
        if (byYear != 0) return byYear;
        return Integer.compare(a.getAge(), b.getAge());
    }
}