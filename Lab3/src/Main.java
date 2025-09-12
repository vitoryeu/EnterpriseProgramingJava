import java.util.*;

class MyList<E> implements Iterable<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public MyList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public void add(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public boolean remove(E e) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], e)) {
                System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                elements[--size] = null;
                return true;
            }
        }
        return false;
    }

    public void addAll(Collection<? extends E> c) {
        for (E e : c) add(e);
    }

    public void removeAll(Collection<?> c) {
        for (Object o : c) remove((E) o);
    }

    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    public void sort(Comparator<? super E> comp) {
        Arrays.sort((E[]) Arrays.copyOf(elements, size), comp);
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int cursor = 0;
            public boolean hasNext() { return cursor < size; }
            public E next() { return (E) elements[cursor++]; }
        };
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elements, size));
    }
}

public class Main {
    public static void main(String[] args) {
        MyList<String> list = new MyList<>();
        list.add("Banana");
        list.add("Apple");
        list.add("Orange");

        System.out.println("List: " + list);
        list.remove("Apple");
        System.out.println("After remove: " + list);

        list.addAll(List.of("Mango", "Pineapple"));
        System.out.println("After addAll: " + list);

        list.sort(Comparator.naturalOrder());
        System.out.println("Sorted: " + list);

        for (String s : list) {
            System.out.println("Iterating: " + s);
        }

        list.clear();
        System.out.println("Cleared: " + list + " (size=" + list.size() + ")");
    }
}