import Objects.*;
import Abstractions.*;

public class Main {
    public static void main(String[] args) {
        Shape circle = new Circle("Red", 5);
        Shape rectangle = new Rectangle("Blue", 4, 6);
        Shape triangle = new Triangle("Green", 3, 4, 5);

        // Вивід інформації
        System.out.println(circle);
        System.out.println(rectangle);
        System.out.println(triangle);

        // Порівняння за площею
        System.out.println("\nComparison by area:");
        if (circle.compareTo(rectangle) > 0) {
            System.out.println("Circle is larger than Rectangle");
        } else {
            System.out.println("Rectangle is larger than Circle");
        }
    }
}