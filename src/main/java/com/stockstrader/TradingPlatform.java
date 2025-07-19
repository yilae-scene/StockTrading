package com.stockstrader;

import com.google.gson.Gson;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class TradingPlatform {
    //Using a constant for the filename
    private static final String USER_FILE = "user.json";
    //creat a Gson object to be reused
    private static final Gson gson = new Gson();


    // let's defining these here, we can easily change the table layout in one place.
    private static final String HEADER_FORMAT = "%-10s | %-8s | %-15s | %-15s | %-15s | %-15s%n";
    private static final String ROW_FORMAT = "%-10s | %-8d | $%-14.2f | $%-14.2f | $%-14.2f | $%-+15.2f%n";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Market market = initializeMarket();
        User user = loadUser();

        System.out.println("Welcome to the Stock Trading Platform, " +
                user.getUserName() + " !");

        //Main application running
        boolean isRunning = true;
        while (isRunning) {
            displayMenu();
            System.out.println("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> displayMarketSummary(market);
                    case 2 -> displayPortfolio(user, market);
                    case 3 -> handleBuy(user, market, scanner);
                    case 4 -> handleSell(user, market, scanner);
                    case 5 -> market.currentUpdatedPrice();
                    case 6 -> isRunning = false;
                    default -> System.out.println("Invalid choice, please choose only from numbers 1 to 6");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please input only a number");
                //clear the buffer input
                scanner.nextLine();
            }
        }
        //save and exit
        saveUser(user);
        System.out.println("Thank you for using this platform.");
        System.out.println("Your portfolio hase been saved");
        scanner.close();// for better safety
    }

    // Main display Menu for user interaction,  i.e. our user interface lies in here
    private static void displayMenu() {
        System.out.println("\n------- Stock Trading Platform --------");
        System.out.println("1. Display Market Summary");
        System.out.println("2. Display My Portfolio");
        System.out.println("3. Buy Stocks");
        System.out.println("4. Sell Stocks");
        System.out.println("5. Wait for Market Update");
        System.out.println("6. Save and Exit");
        System.out.println("-".repeat(20));
    }

    //display the current market prices
    private static void displayMarketSummary(Market market) {
        System.out.println(" ---- Current Market Prices ----");
        for (Stock stock : market.getAllStocks()) {
            //format the string to display the price in 2 decimal places
            System.out.println(stock.getStockSymbol()
                    + ": $" + String.format("%.2f", stock.getPrice()));
        }
    }

    //diplay individual stock holdings
    private static void displayPortfolio(User user, Market market) {
        System.out.println("---" + user.getUserName() + "'s Portfolio");
        System.out.println("CASH BALANCE: $" + String.format("%.2f", user.getBalance()));
        System.out.println("--- STOCK HOLDINGS ---");
        System.out.println();
        //check if the user have a portfolio first
        if (user.getPortfolio().isEmpty()) {
            String sadFace = "\\u2639\\uFE0";
            System.out.println(" You don't own any stocks currently. sadFace");
            return;
        }
        //claculate totals
        double totalPortfolioValue = 0;
        double totalCostAccumilated = 0;
        System.out.println("-".repeat(25));
        //format to be easily understandable for users.
        System.out.printf(HEADER_FORMAT,
                "Symbol", "Quantity", "Avg. Price",
                "Current Price", "Market Value", "PROFIT/LOSS");

        System.out.println();
        for (Rank rank : user.getPortfolio().values()) {
            String symbol = rank.getStockSymbol();
            int quantity = rank.getQuantity();
            double avgPrice = rank.getAveragePurchasePrice();
            Stock stock = market.getStock(symbol);
            double currentPrice = (stock != null) ? stock.getPrice() : 0.0;
            double totalCost = quantity * avgPrice;
            double marketValue = quantity * currentPrice;
            double profit_loss = marketValue - totalCost;

            totalPortfolioValue += marketValue;
            totalCostAccumilated += totalCost;

            //now we can use the ROW_FORMAT for better out put display;
            System.out.printf(ROW_FORMAT, symbol, quantity, avgPrice, currentPrice,
                    marketValue, profit_loss);
        }
        System.out.println();
        double totalPortfolioProfitLoss = totalPortfolioValue - totalCostAccumilated;
        double totalPortfolioWorth = user.getBalance() + totalPortfolioValue;

        System.out.printf("Total Portfolio Market Value: $%.2f%n", totalPortfolioValue);
        System.out.printf("Total Portfolio Profit/Loss:  $%.2f%n", totalPortfolioProfitLoss);
        System.out.printf("Total Net Worth (Cash + Stocks): $%.2f%n", totalPortfolioWorth);
        System.out.println();

    }

    //Buy stocks
    private static void handleBuy(User user, Market market, Scanner scanner) {
        System.out.println("Enter stock symbol to buy: ");
        //make sure that they inputs are uppercase
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("Error!!!: " + symbol + " not found. Make sure the spelling is correct");
            return;
        }
        System.out.println("QUANTITY TO BUY: ");
        try {
            int quantity = scanner.nextInt();
            scanner.nextLine();// buffer the new line
            if (quantity <= 0) {
                System.out.println("quantity values must be greater than 0");
            } else {
                user.buyStock(symbol, quantity, stock.getPrice());
            }
        } catch (InputMismatchException e) {
            System.out.println("please Enter only a whole number");
            scanner.nextLine();//buffer new line
        }
    }

    private static void handleSell(User user, Market market, Scanner scanner) {
        System.out.println("Which stock do you want to sell: ");
        //make sure the inputs are all uppercase
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("Error!!! " + symbol + " doesn't exist");
            return;
        }
        //if it exists
        System.out.println("How many shares do you want to sell: ");
        try {
            int quantity = scanner.nextInt();
            scanner.nextLine();//buffer new line
            if (quantity <= 0) {
                System.out.println("quantity to sell must be above 0");
            } else {
                user.sellStock(symbol, quantity, stock.getPrice());
            }
        } catch (InputMismatchException e) {
            System.out.println("ERROR!!! please input a correct value [i.e numbers above 0].");
            scanner.nextLine();//buffer new line
        }
    }

    // --- lets initialize a market to simulate the stock exchange world ---

    private static Market initializeMarket() {
        Market market = new Market();
        market.addStock(new Stock("AAPL", 150.75));
        market.addStock(new Stock("GOOG", 2750.40));
        market.addStock(new Stock("TSLA", 780.22));
        market.addStock(new Stock("AMZN", 3400.65));
        return market;
    }

    //To write our new user on a files as gson
    private static void saveUser(User user) {
        try (Writer writer = new FileWriter(USER_FILE)) {
            gson.toJson(user, writer);
        } catch (IOException e) {
            System.out.println("Error: Could not save user data.");
            e.printStackTrace();
        }
    }

    private static User loadUser() {
        try (Reader reader = new FileReader(USER_FILE)) {
            // Read the JSON file and convert it back to a User object
            User user = gson.fromJson(reader, User.class);
            System.out.println("Loaded existing user data.");
            return user;
        } catch (FileNotFoundException e) {
            // This is not an error, it just means it's the first time running.
            System.out.println("No existing user data found. Creating a new user.");
            return new User(0.0, "John Doe"); // Create a default user
        } catch (IOException e) {
            System.out.println("Error: Could not read user data. Starting with a new user.");
            e.printStackTrace();
            return new User(0.0, "John Doe");
        }
    }
}

