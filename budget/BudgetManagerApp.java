package budget;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class BudgetManagerApp {
    private final Scanner scan = new Scanner(System.in);
    private final Set<Product> products;

    public BudgetManagerApp() {
        this.products = new LinkedHashSet<>();
    }

    public void run() {
        while (scan.hasNext()) {
            String productAndPrice = scan.nextLine();
            int lastSpaceIndex = productAndPrice.lastIndexOf(" ");
            String productName = productAndPrice.substring(0, lastSpaceIndex);
            double productPrice = Double.parseDouble(productAndPrice.substring(lastSpaceIndex + 2));
            products.add(new Product(productName, productPrice));
        }
        scan.close();

        products.forEach(System.out::println);
        System.out.println();
        System.out.printf("Total: $%.2f\n", getTotal());
    }

    private double getTotal() {
        return products.stream()
                .map(Product::getPrice)
                .reduce(Double::sum)
                .orElse(0.0);
    }
}
