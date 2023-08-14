package budget;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class BudgetManagerApp {
    private final Scanner scan = new Scanner(System.in);
    private final Set<Purchase> purchases;
    private double income;
    private boolean isRunning;

    public BudgetManagerApp() {
        this.purchases = new LinkedHashSet<>();
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

    private double getTotal(Set<Purchase> purchases) {
        return purchases.stream()
                .map(Purchase::getPrice)
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
            String userOption;
            do {
                System.out.println("""
                        Choose the type of purchase
                        1) Food
                        2) Clothes
                        3) Entertainment
                        4) Other
                        5) Back""");
                userOption = scan.nextLine();

                if ("5".equals(userOption)) {
                    break;
                }

                Purchase.Category category = getCategoryFromOption(userOption);
                System.out.println("\nEnter purchase name:");
                String name = scan.nextLine();
                System.out.println("Enter its price:");
                double price = Double.parseDouble(scan.nextLine());

                purchases.add(new Purchase(name, price, category));
                income -= price;
                System.out.println("Purchase was added!");

                System.out.println();
            } while (true);

        }
    }

    private Purchase.Category getCategoryFromOption(String userOption) {
        return switch (userOption) {
            case "1" -> Purchase.Category.Food;
            case "2" -> Purchase.Category.Clothes;
            case "3" -> Purchase.Category.Entertainment;
            case "4" -> Purchase.Category.Other;
            default -> Purchase.Category.All;
        };
    }

    private class ListPurchasesCommand extends Command {

        public ListPurchasesCommand(String name) {
            super(name);
        }

        @Override
        void execute() {
            if (purchases.size() > 0) {
                String userOption;
                do {
                    System.out.println("""
                        Choose the type of purchases
                        1) Food
                        2) Clothes
                        3) Entertainment
                        4) Other
                        5) All
                        6) Back""");

                    userOption = scan.nextLine();

                    if ("6".equals(userOption)) {
                        break;
                    }

                    Purchase.Category category = getCategoryFromOption(userOption);
                    System.out.println();
                    System.out.println(category + ":");

                    if (category == Purchase.Category.All) {
                        purchases.forEach(System.out::println);
                        System.out.printf("Total sum: $%.2f\n", getTotal(purchases));
                    } else {
                        Set<Purchase> catPurchases = getPurchasesByCategory(category);
                        if (catPurchases.isEmpty()) {
                            System.out.println("The purchase list is empty");
                        } else {
                            catPurchases.forEach(System.out::println);
                            System.out.printf("Total sum: $%.2f\n", getTotal(catPurchases));
                        }
                    }
                    System.out.println();
                } while (true);
            } else {
                System.out.println("The purchase list is empty");
            }
        }
    }

    private Set<Purchase> getPurchasesByCategory(Purchase.Category category) {
        return purchases.stream()
                .filter(p -> p.getCategory() == category)
                .collect(Collectors.toSet());
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
