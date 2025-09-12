import Models.*;
import Constants.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Restaurant r = new Restaurant();

        r.addMenuItem(new MenuItem(1, "Tomato Soup", 4.50, Category.STARTER, true));
        r.addMenuItem(new MenuItem(2, "Grilled Chicken", 11.90, Category.MAIN, true));
        r.addMenuItem(new MenuItem(3, "Cheesecake", 5.20, Category.DESSERT, true));
        r.addMenuItem(new MenuItem(4, "Latte", 3.80, Category.DRINK, false)); // недоступний

        Customer alice = new Customer(100, "Alice", false);
        Customer bob   = new Customer(101, "Bob", true); // постійний клієнт (10%)

        Order o1 = r.createOrder(alice);
        r.addItem(o1.getId(), 1, 1);
        r.addItem(o1.getId(), 2, 1);
        System.out.println(o1);

        try {
            r.addItem(o1.getId(), 4, 2);
        } catch (Exception e) {
            System.out.println("WARN: " + e.getMessage());
        }

        r.updateQuantity(o1.getId(), 1, 2);
        r.removeItem(o1.getId(), 2);
        System.out.println(o1);

        r.changeStatus(o1.getId(), OrderStatus.IN_PROGRESS);
        r.changeStatus(o1.getId(), OrderStatus.READY);
        System.out.println("Status: " + r.getOrder(o1.getId()).getStatus());


        r.cancelOrder(o1.getId());
        System.out.println("After cancel: " + r.getOrder(o1.getId()));

        Order o2 = r.createOrder(bob);
        r.addItem(o2.getId(), 2, 2);
        r.addItem(o2.getId(), 3, 1);
        System.out.println("LOYAL order: " + o2);
        System.out.println("Final total (with discount): " + o2.getTotalPrice());
    }
}