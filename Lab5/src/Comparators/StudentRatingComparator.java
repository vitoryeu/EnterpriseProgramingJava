package Comparators;

import Models.*;
import java.util.*;

public class StudentRatingComparator implements Comparator<Student> {
    @Override public int compare(Student a, Student b) {
        int byFaculty = a.getFaculty().compareToIgnoreCase(b.getFaculty());
        if (byFaculty != 0) return byFaculty;
        int byAvg = -Double.compare(a.getAverageGrade(), b.getAverageGrade());
        if (byAvg != 0) return byAvg;
        int byYear = Integer.compare(a.getYearOfStudy(), b.getYearOfStudy());
        if (byYear != 0) return byYear;
        return a.getName().compareToIgnoreCase(b.getName());
    }
}