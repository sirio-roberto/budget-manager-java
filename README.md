# Budget Manager App

The Budget Manager App is a Java application that allows users to manage their budget by tracking income and purchases. The app provides a menu-driven interface for users to perform various actions such as adding income, adding purchases, listing purchases, checking the balance, saving and loading data, and analyzing purchases.
## Features

- `Add income`: Allows users to input additional income and updates the total income amount.

- `Add purchase`: Enables users to add purchases with categories (food, clothes, entertainment, other) and deducts the purchase amount from the income.

- `Show list of purchases`: Displays a list of purchases categorized by type and provides the total amount spent on each category.

- `Balance`: Displays the current balance by showing the remaining income after deducting all purchases.

- `Save`: Saves the current state of income and purchases to a file named "purchases.txt".

- `Load`: Loads previously saved data from the "purchases.txt" file, restoring the income and purchase history.

- `Analyze (Sort)`: Offers sorting options to analyze purchases. Users can sort all purchases, sort by purchase type, sort certain types, or go back to the main menu.

- `Exit`: Closes the application and displays a goodbye message.

## Usage

1. Compile the Java files:

```
javac budget/*.java
```

2. Run the Main class to start the application:

```
java budget.Main
```

3. Follow the on-screen instructions to interact with the application. Use the displayed menu to choose actions, such as adding income, adding purchases, listing purchases, and more.

4. To exit the application, select option 0 from the menu.

## Example

```
Choose your action:
1) Add income
2) Add purchase
3) Show list of purchases
4) Balance
5) Save
6) Load
7) Analyze (Sort)
0) Exit
> 7

How do you want to sort?
1) Sort all purchases
2) Sort by type
3) Sort certain type
4) Back
> 1

The purchase list is empty!

...

Choose your action:
1) Add income
2) Add purchase
3) Show list of purchases
4) Balance
5) Save
6) Load
7) Analyze (Sort)
0) Exit
> 0

Bye!
```

Classes and Packages

- `budget`: Contains the main application classes.
    - `BudgetManagerApp`: The main class that manages the application flow.
    - `Main`: The entry point of the application.
    - `Purchase`: Represents a purchase and its details.