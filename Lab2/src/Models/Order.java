package Models;

import Constants.*;
import java.util.*;
import java.util.stream.Collectors;

public class Order {
    private final int id;
    private final Customer customer;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status = OrderStatus.NEW;
    private double totalPrice;

    public Order(int id, Customer customer) {
        this.id = id;
        this.customer = Objects.requireNonNull(customer);
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<OrderLine> getLines() { return Collections.unmodifiableList(lines); }
    public OrderStatus getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }

    void addOrIncrease(MenuItem item, int qty) {
        var existing = lines.stream().filter(l -> l.getItem().getId() == item.getId()).findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + qty);
        } else {
            lines.add(new OrderLine(item, qty));
        }
    }

    void updateQuantity(int itemId, int qty) {
        var line = lines.stream().filter(l -> l.getItem().getId() == itemId).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Item not found in order"));
        line.setQuantity(qty);
    }

    void removeItem(int itemId) {
        boolean removed = lines.removeIf(l -> l.getItem().getId() == itemId);
        if (!removed) throw new NoSuchElementException("Item not found in order");
    }

    void setStatus(OrderStatus newStatus) {
        if (status == OrderStatus.CANCELED || status == OrderStatus.COMPLETED)
            throw new IllegalStateException("Order is final and cannot be changed");
        this.status = newStatus;
    }

    void cancel() {
        if (status == OrderStatus.COMPLETED)
            throw new IllegalStateException("Completed order cannot be canceled");
        this.status = OrderStatus.CANCELED;
    }

    void recalcTotal(double discountPercent) {
        double sum = lines.stream().mapToDouble(OrderLine::lineTotal).sum();
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be 0..100");
        }
        double discount = sum * (discountPercent / 100.0);
        this.totalPrice = round2(sum - discount);
    }

    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    @Override public String toString() {
        String items = lines.stream().map(Object::toString).collect(Collectors.joining("; "));
        return "Order #%d [%s] for %s | Items: [%s] | Total: %.2f"
                .formatted(id, status, customer.getName(), items, totalPrice);
    }
}