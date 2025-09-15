package Comparators;

import Models.*;
import java.util.*;

public class ComparatorByAgeAsc implements Comparator<Student> {
    @Override public int compare(Student a, Student b) { return Integer.compare(a.getAge(), b.getAge()); }
}
