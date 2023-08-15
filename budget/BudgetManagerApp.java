package budget;

import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManagerApp {
    private final Scanner scan = new Scanner(System.in);
    private final String FILE_PATH = "purchases.txt";
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
            case "1" -> new AddIncomeCommand();
            case "2" -> new AddPurchaseCommand();
            case "3" -> new ListPurchasesCommand();
            case "4" -> new ShowBalanceCommand();
            case "5" -> new SaveCommand();
            case "6" -> new LoadCommand();
            case "7" -> new AnalyzeCommand();
            default -> new ExitCommand();
        };
    }

    private void showMenu() {
        System.out.println("""
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                5) Save
                6) Load
                7) Analyze (Sort)
                0) Exit""");
    }

    private double getTotal(Collection<Purchase> purchases) {
        return purchases.stream()
                .map(Purchase::getPrice)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    abstract private static class Command {
        public Command() {
        }

        abstract void execute();
    }

    private class AddIncomeCommand extends Command {

        @Override
        void execute() {
            System.out.println("Enter income:");
            income += Double.parseDouble(scan.nextLine());
            System.out.println("Income was added!");
        }
    }

    private class AddPurchaseCommand extends Command {

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
        @Override
        void execute() {
            System.out.printf("Balance: $%.2f\n", income);
        }
    }

    private class SaveCommand extends Command {

        @Override
        void execute() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                writer.write(String.valueOf(income));
                writer.newLine();
                for (Purchase p : purchases) {
                    writer.write(new Gson().toJson(p));
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Purchases were saved!");
        }
    }

    private class LoadCommand extends Command {

        @Override
        void execute() {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String incomeStr;
                if ((incomeStr = reader.readLine()) != null) {
                    income = Double.parseDouble(incomeStr);
                }
                purchases.addAll(reader.lines()
                        .map(l -> new Gson().fromJson(l, Purchase.class))
                        .collect(Collectors.toSet()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Purchases were loaded!");
        }
    }

    private class AnalyzeCommand extends Command {

        @Override
        void execute() {
            String userOption;
            do {
                System.out.println("""
                        How do you want to sort?
                        1) Sort all purchases
                        2) Sort by type
                        3) Sort certain type
                        4) Back""");
                userOption = scan.nextLine();

                if ("4".equals(userOption)) {
                    break;
                }

                System.out.println();
                switch (userOption) {
                    case "1" -> sortAll();
                    case "2" -> sortByType();
                    case "3" -> sortCertainType();
                }

                System.out.println();
            } while (true);
        }

        private void sortCertainType() {
            System.out.println("""
                    Choose the type of purchase
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other""");
            String userOption = scan.nextLine();
            System.out.println();

            Purchase.Category category = getCategoryFromOption(userOption);
            List<Purchase> catPurchases = purchases.stream()
                            .filter(p -> p.getCategory() == category)
                            .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                            .toList();

            if (catPurchases.isEmpty()) {
                System.out.println("The purchase list is empty!");
            } else {
                System.out.println(category + ":");
                catPurchases.forEach(System.out::println);
                System.out.printf("Total sum: $%.2f\n", getTotal(catPurchases));
            }
        }

        private void sortByType() {
            Map<Purchase.Category, Double> catWithTotalMap = new HashMap<>();

            System.out.println("Types:");
            for (Purchase.Category category : Purchase.Category.values()) {
                if (category == Purchase.Category.All) {
                    continue;
                }
                double totalPrice = purchases.stream()
                        .filter(p -> p.getCategory() == category)
                        .map(Purchase::getPrice)
                        .reduce(Double::sum)
                        .orElse(0.0);
                catWithTotalMap.put(category, totalPrice);
            }

            catWithTotalMap.keySet().stream()
                    .sorted((k1, k2) -> Double.compare(catWithTotalMap.get(k2), catWithTotalMap.get(k1)))
                    .forEach(k -> {
                        if (catWithTotalMap.get(k) == 0.0) {
                            System.out.println(k + " - $0");
                        } else {
                            System.out.printf("%s - $%.2f\n", k, catWithTotalMap.get(k));
                        }
                    });

            double totalPrice = getTotal(purchases);
            if (totalPrice == 0.0) {
                System.out.println("Total sum: $0");
            } else {
                System.out.printf("Total sum: $%.2f\n", totalPrice);
            }
        }

        private void sortAll() {
            if (purchases.isEmpty()) {
                System.out.println("The purchase list is empty!");
            } else {
                System.out.println("All:");
                purchases.stream()
                        .sorted(Comparator.comparing(Purchase::getCategory))
                        .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                        .forEach(System.out::println);

                System.out.printf("Total: $%.2f\n", getTotal(purchases));
            }
        }
    }

    private class ExitCommand extends Command {

        @Override
        void execute() {
            isRunning = false;
            scan.close();

            System.out.println("Bye!");
        }
    }
}
