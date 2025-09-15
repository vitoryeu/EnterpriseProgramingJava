package Utils;

import Models.*;
import java.util.*;
import java.util.function.Predicate;

public class StudentUtils {
    public static List<Student> sort(List<Student> list, Comparator<Student> cmp) {
        return list.stream().sorted(cmp).toList();
    }
    public static List<Student> topN(Collection<Student> list, Comparator<Student> cmp, int n) {
        return list.stream().sorted(cmp).limit(n).toList();
    }
    public static List<Student> filter(Collection<Student> list, Predicate<Student> predicate) {
        return list.stream().filter(predicate).toList();
    }
    public static List<Student> filterAndSort(
            Collection<Student> list, Predicate<Student> predicate, Comparator<Student> cmp) {
        return list.stream().filter(predicate).sorted(cmp).toList();
    }
}
