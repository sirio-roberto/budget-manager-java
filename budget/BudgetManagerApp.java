package budget;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class BudgetManagerApp {
    private final Scanner scan = new Scanner(System.in);
    private final Set<Product> products;
    private double income;
    private boolean isRunning;

    public BudgetManagerApp() {
        this.products = new LinkedHashSet<>();
        income = 0.0;
    }

    public void run() {
        isRunning = true;
        while (isRunning) {
            showMenu();
            String userAction = scan.nextLine();
            System.out.println();

            getCommandFromUserInput(userAction).execute();
            System.out.println();
        }
    }

    private Command getCommandFromUserInput(String userAction) {
        return switch (userAction) {
            case "1" -> new AddIncomeCommand("addIncome");
            case "2" -> new AddPurchaseCommand("addPurchase");
            case "3" -> new ListPurchasesCommand("listPurchases");
            case "4" -> new ShowBalanceCommand("showBalance");
            default -> new ExitCommand("exit");
        };
    }

    private void showMenu() {
        System.out.println("""
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                0) Exit""");
    }

    private double getTotal() {
        return products.stream()
                .map(Product::getPrice)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    abstract private static class Command {
        private final String name;

        public Command(String name) {
            this.name = name;
        }

        abstract void execute();
    }

    private class AddIncomeCommand extends Command {

        public AddIncomeCommand(String name) {
            super(name);
        }

        @Override
        void execute() {
            System.out.println("Enter income:");
            income += Double.parseDouble(scan.nextLine());
            System.out.println("Income was added!");
        }
    }

    private class AddPurchaseCommand extends Command {

        public AddPurchaseCommand(String name) {
            super(name);
        }

        @Override
        void execute() {
            System.out.println("Enter purchase name:");
            String name = scan.nextLine();
            System.out.println("Enter its price:");
            double price = Double.parseDouble(scan.nextLine());

            products.add(new Product(name, price));
            income -= price;
            System.out.println("Purchase was added!");
        }
    }

    private class ListPurchasesCommand extends Command {

        public ListPurchasesCommand(String name) {
            super(name);
        }

        @Override
        void execute() {
            if (products.size() > 0) {
                products.forEach(System.out::println);
                System.out.printf("Total sum: $%.2f\n", getTotal());
            } else {
                System.out.println("The purchase list is empty");
            }
        }
    }

    private class ShowBalanceCommand extends Command {

        public ShowBalanceCommand(String name) {
            super(name);
        }

        @Override
        void execute() {
            System.out.printf("Balance: $%.2f\n", income);
        }
    }

    private class ExitCommand extends Command {

        public ExitCommand(String name) {
            super(name);
        }

        @Override
        void execute() {
            isRunning = false;
            scan.close();

            System.out.println("Bye!");
        }
    }
}
