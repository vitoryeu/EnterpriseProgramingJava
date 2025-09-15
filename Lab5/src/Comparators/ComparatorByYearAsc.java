package Comparators;

import Models.*;
import java.util.*;

public class ComparatorByYearAsc implements Comparator<Student> {
    @Override public int compare(Student a, Student b) { return Integer.compare(a.getYearOfStudy(), b.getYearOfStudy()); }
}