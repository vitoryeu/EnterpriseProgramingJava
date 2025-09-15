package Comparators;

import Models.*;
import java.util.*;

public class ComparatorByAvgDesc implements Comparator<Student> {
    @Override public int compare(Student a, Student b) { return -Double.compare(a.getAverageGrade(), b.getAverageGrade()); }
}