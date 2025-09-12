package Models;

import Constants.*;
import java.util.*;

public class Restaurant {
    private final Map<Integer, MenuItem> menu = new HashMap<>();
    private final Map<Integer, Order> orders = new HashMap<>();
    private int nextOrderId = 1;

    public void addMenuItem(MenuItem item) {
        if (menu.containsKey(item.getId())) throw new IllegalArgumentException("Duplicate menu item id");
        menu.put(item.getId(), item);
    }
    public void setAvailability(int itemId, boolean available) {
        getMenuItem(itemId).setAvailable(available);
    }
    public MenuItem getMenuItem(int itemId) {
        var item = menu.get(itemId);
        if (item == null) throw new NoSuchElementException("Menu item not found");
        return item;
    }
    public Collection<MenuItem> getMenu() { return Collections.unmodifiableCollection(menu.values()); }

    public Order createOrder(Customer customer) {
        int id = nextOrderId++;
        Order order = new Order(id, customer);
        orders.put(id, order);
        // початковий total = 0
        order.recalcTotal(applyDiscountPercent(customer));
        return order;
    }

    public Order getOrder(int orderId) {
        var o = orders.get(orderId);
        if (o == null) throw new NoSuchElementException("Order not found");
        return o;
    }

    public void addItem(int orderId, int itemId, int quantity) {
        Order order = getOrder(orderId);
        ensureEditable(order);
        MenuItem item = getMenuItem(itemId);
        if (!item.isAvailable()) throw new IllegalStateException("Item is unavailable: " + item.getName());
        order.addOrIncrease(item, quantity);
        order.recalcTotal(applyDiscountPercent(order.getCustomer()));
    }

    public void updateQuantity(int orderId, int itemId, int quantity) {
        Order order = getOrder(orderId);
        ensureEditable(order);
        order.updateQuantity(itemId, quantity);
        order.recalcTotal(applyDiscountPercent(order.getCustomer()));
    }

    public void removeItem(int orderId, int itemId) {
        Order order = getOrder(orderId);
        ensureEditable(order);
        order.removeItem(itemId);
        order.recalcTotal(applyDiscountPercent(order.getCustomer()));
    }

    public void changeStatus(int orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
    }

    public void cancelOrder(int orderId) {
        Order order = getOrder(orderId);
        order.cancel();
    }

    private void ensureEditable(Order order) {
        if (order.getStatus() != OrderStatus.NEW && order.getStatus() != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Order cannot be edited in status: " + order.getStatus());
    }

    private double applyDiscountPercent(Customer c) {
        return c.isLoyal() ? 10.0 : 0.0;
    }
}