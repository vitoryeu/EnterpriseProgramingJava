package Abstractions;

public abstract class Shape implements Comparable<Shape> {
    private String color;

    public Shape(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public abstract double calculateArea();
    public abstract double calculatePerimeter();

    @Override
    public int compareTo(Shape other) {
        return Double.compare(this.calculateArea(), other.calculateArea());
    }

    @Override
    public String toString() {
        return String.format("%s (Color: %s, Area: %.2f, Perimeter: %.2f)",
                this.getClass().getSimpleName(), color, calculateArea(), calculatePerimeter());
    }
}