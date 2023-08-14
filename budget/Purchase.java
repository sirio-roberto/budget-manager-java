package budget;

import java.util.EnumSet;
import java.util.Objects;

public class Purchase {
    private String name;
    private double price;

    private Category category;

    public Purchase(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Purchase(String name, double price, Category category) {
        this(name, price);
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(name, purchase.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, price);
    }

    public enum Category {
        Food, Clothes, Entertainment, Other, All
    }
}
