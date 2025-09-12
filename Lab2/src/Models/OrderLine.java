package Models;

import java.util.*;

public class OrderLine {
    private final MenuItem item;
    private int quantity;

    public OrderLine(MenuItem item, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.item = Objects.requireNonNull(item);
        this.quantity = quantity;
    }

    public MenuItem getItem() { return item; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }

    public double lineTotal() { return item.getPrice() * quantity; }

    @Override public String toString() {
        return "%s x%d = %.2f".formatted(item.getName(), quantity, lineTotal());
    }
}
