package com.stockstrader;

/* a class to represent single stock instance
@param tickerSymbol (the stocks name) => String
@param price (price of shares) => double
@param quantity (the amount of shares) => int;

the method would also have getters and setters for price and quantity
 */

public class Stock {
    private final String stockSymbol; // final because name won't change
    private double price; // current market price;
    //private int quantity;

    public Stock(String stockSymbol, double initialPrice) {
        this.stockSymbol = stockSymbol;
        price = initialPrice;
    }

    public String getStockSymbol(){
        return stockSymbol;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double newPrice){
        if(newPrice > 0){
            price = newPrice;
        }
    }
}
