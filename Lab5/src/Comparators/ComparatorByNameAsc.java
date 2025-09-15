package Comparators;

import Models.*;
import java.util.*;

public class ComparatorByNameAsc implements Comparator<Student> {
    @Override public int compare(Student a, Student b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}