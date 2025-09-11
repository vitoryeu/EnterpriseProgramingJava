package Objects;

import Abstractions.*;

public class Triangle extends Shape {
    private double a, b, c;

    public Triangle(String color, double a, double b, double c) {
        super(color);

        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Invalid triangle sides!");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double calculateArea() {
        double p = calculatePerimeter() / 2;

        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    @Override
    public double calculatePerimeter() {
        return a + b + c;
    }
}
