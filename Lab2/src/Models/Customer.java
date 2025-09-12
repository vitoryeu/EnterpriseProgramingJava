package Models;

import java.util.*;

public class Customer {
    private final int id;
    private final String name;
    private final boolean loyal; // постійний клієнт

    public Customer(int id, String name, boolean loyal) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.loyal = loyal;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isLoyal() { return loyal; }
}