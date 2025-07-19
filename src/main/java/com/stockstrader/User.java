package com.stockstrader;

/*this class will represent a single user instance
will also have:
@param userName => String;
@param balance => double;

# for tracking stocks Map seemed an ideal choice which have the advantages
of pairing Key: Value. In our case it will be ticketSymbol which is String,
with Integer (shares the user owns).
    will have this feature Map(String, Integer) = Map(ticketSymbol, amountOfShares);
  Map is chosen because we don't have to create objects for each shares, except to put it
  in a Map. This makes it more efficient.

The User class will be responsible for managing everything related to
a single user: their identity, their cash balance, and the stocks they own.
 */

import java.util.HashMap;
import java.util.Map;

public class User {
    private final String userName;
    private double balance;

    /*in our portfolio of users, we use stock symbol (String) to find
     how many shares we own (Integer).
     Example: {"AAPL": 10, "GOOG": 5}
    meaning 10 shares of Apple, 5 shares of Google.
     */

    //private final Map<String, Integer> portfolio = new HashMap<>();
    //In order to get a better glimpse on not only the stocks they own but also
    //the money they paid, it is better to use a map that contains the Symbol (string)
    //and Rank object. [map(symbol, rank)]

    private final Map<String, Rank> portfolio = new HashMap<>();

    //we need now default constructor for gson



    public User(double balance, String userName) {
        this.balance = balance;
        this.userName = userName;
    }
    public User() {
            userName = "John Doe";
    }


    public String getUserName() {
        return userName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Map<String, Rank> getPortfolio() {
        return portfolio;
    }

    /*a boolean methods to buy and sell stocks
    with name of stock, quantity and market value as parameters
    in buyStock method = stock and quantity will be used to identify
    the stock and number of shares the user wants to purchase.
    Market will check the current value of the stock per share.

    in sellStock method = stock and quantity will be used to identify
    the stock and number of shares the user wants to sell by checking his/her portfolio.
    Market will check the current value of the stock per share.
     */

    public boolean buyStock(String stockSymbol, int quantity, double pricePerShare) {
        double totalCost = quantity * pricePerShare;


        if (this.balance >= totalCost) {
            //enough money exists
            this.balance -= totalCost;

            /*we need to add shares to our portfolio
            to do this safely, we will use the 'containsKey' instead of the
            'getOrDefault' we used with integer.
            to check if the stock exists before or not. if it doesn't
            exist it creates
            one with the returned value from the getOrDefault method.
             */
            if (this.portfolio.containsKey(stockSymbol)) {
                Rank existingPosition = this.portfolio.get(stockSymbol);
                //we need to calculate old values

                int previousQuantity = existingPosition.getQuantity();
                double previousAvgPrice = existingPosition.getAveragePurchasePrice();
                double previousTotalValue = previousQuantity * previousAvgPrice;

                //we need to know our new values
                int newQuantity = previousQuantity + quantity;
                double newTotalValue = previousTotalValue + totalCost;
                double newAvgPrice = newTotalValue / newQuantity;

                //so now we nee to update the Rank with new values
                existingPosition.setQuantity(newQuantity);
                existingPosition.setAveragePurchasePrice(newAvgPrice);
            } else {
                // this must be the fist time buying this king of stock
                Rank newPosition = new Rank(stockSymbol, quantity, pricePerShare);
                this.portfolio.put(stockSymbol, newPosition);
            }
            System.out.println("Successfully purchased " + quantity
                    + " amount of " + stockSymbol);
            return true;

        } else {
            //Insufficient balance
            System.out.println("Not enough balance to purchase those " + quantity +
                    " of " + stockSymbol);
            return false;
        }
    }

    //returns true if sell was successful or false if sell was unsuccessful.
    //we have refactored it the same as the buyStock method to add Rank in the
    //Map instead of Integer.
    public boolean sellStock(String stockSymbol, int quantity, double pricePerShare) {
        // we will use the same getOrDefault method as buy to check
        //change 'getORDefault' with 'containsKey' as the value in Map is OBJECT.
        //if the stockSymbol we are selling do exist or if we have
        //enough amount

        if (!this.portfolio.containsKey(stockSymbol)) {
            System.out.println("You don't own any shares of " + stockSymbol);
            return false;
        }
        Rank position = this.portfolio.get(stockSymbol);
        int currentShares = position.getQuantity();

        //check if the user owns enough shares to sell
        if (currentShares > quantity) {
            //update our balance
            double totalSaleValue = quantity * pricePerShare;
            this.balance += totalSaleValue;

            //update the users share by deducting from the sold shares
            int updatedShares = currentShares - quantity;
            if (updatedShares == 0) {
                //if nothing is left remove it from the portfolio
                portfolio.remove(stockSymbol);
            } else {
                position.setQuantity(updatedShares);
            }

            System.out.println("Transaction successful " +
                    "sold " + quantity + " shares of " +
                    stockSymbol);
            return true;
        } else {
            //don't have the requested amount of shares for the given stock
            System.out.println("Transaction Failed you own " +
                    currentShares + " of " + stockSymbol);
            return false;
        }
    }
}

