package com.stockstrader;
/* This class is where we can calculate the user
profit and loss. It also a class the represents a
user's holdings in a single stock. We only use this class
to hold data
 */
public class Rank {

    //a user can buy the same stock multiple times
    private String stockSymbol;
    private int quantity;
    private double averagePurchasePrice;

    public Rank(String stockSymbol, int quantity, double purchasePrice){
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        averagePurchasePrice = purchasePrice;
    }

    //default constructor so that it can work directly wiht Gson library
    public Rank(){

    }

    //To allow classes to read the data
    public String getStockSymbol(){
        return stockSymbol;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getAveragePurchasePrice() {
        return averagePurchasePrice;
    }


    //Help the User class to update a posititon when buying more shares
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setAveragePurchasePrice(double averagePurchasePrice){
        this.averagePurchasePrice = averagePurchasePrice;
    }
}
