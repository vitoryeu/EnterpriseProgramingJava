package Models;

import Constants.*;
import java.util.*;

public class MenuItem {
    private final int id;
    private String name;
    private double price;
    private Category category;
    private boolean available;

    public MenuItem(int id, String name, double price, Category category, boolean available) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.price = price;
        this.category = Objects.requireNonNull(category);
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }
    public boolean isAvailable() { return available; }

    public void setName(String name) { this.name = Objects.requireNonNull(name); }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }
    public void setCategory(Category category) { this.category = Objects.requireNonNull(category); }
    public void setAvailable(boolean available) { this.available = available; }

    @Override public String toString() {
        return "%d: %s (%.2f, %s)%s".formatted(id, name, price, category, available ? "" : " [UNAVAILABLE]");
    }
}