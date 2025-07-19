package com.stockstrader;

/* this is a class that will represent
the stock market.it will simulate price changes as
real stock exchanges do. And holds all available stocks.
- also will hold available stocks as an object.
- the idea is to initially make it update the price of each stock randomly.
[by certain percentage between a certain range, say maybe from -5%  to +5%;
everytime we refresh the market. this will be done by our methods.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Market {
    //we will have a map that holds the
    //stockSymbol as String and the actual stock, making it a lot
    //easier to traverse

    private final Map<String, Stock> availableStocks = new HashMap<>();

    //random number generator to simulate the price of our stocks.
    private final Random randomNum = new Random();

    //Methods of Market

    //adds new stock to be traded

    public void addStock(Stock newStock) {
        availableStocks.put(newStock.getStockSymbol(), newStock);
    }

    //to retrieve a single stock
    public Stock getStock(String symbolOfStock) {
        return availableStocks.get(symbolOfStock);
    }

    //to return all the available stocks
    public ArrayList<Stock> getAllStocks() {
        ArrayList<Stock> allStocks = new ArrayList<>();
        //we need to cast it first to stock type.
        allStocks.add((Stock) (availableStocks.values()));
        return allStocks;
    }

    /*below is a way of simulating the stock market
    at least as far as I know. with fluctuating prices.
    the method goes through each stock an updates it prices
    randomly
     */


    public void currentUpdatedPrice() {
        //lets check the current date and time.
        LocalDateTime currentDateAndTime = LocalDateTime.now();

        //format the currentDateAndTime for better readability
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        String readableDate = currentDateAndTime.format(formatter);
        System.out.println(".....updated price as of " + readableDate);

        //loop through each map with Stock values
        for (Stock st : availableStocks.values()) {
            double currentPrice = st.getPrice();

            /*we need to find a way to calculate random percentage
            in the range of -5% to +5% of original price.
            our random.nextDouble() will provide us with range from
            0.0 to 1.0. if we constantly subtract 0.5 from generate random double
            value we get ranges from (-0.5 to +0.5) then we multiply that
            with 0.1 we get a range of (-0.05  to +0.05) which is exactly
            as hoped will get an up or down price in range of 5%.
             */

            double changeInPrice = (0.5 - randomNum.nextDouble()) * 0.1;

            double updatedPrice = currentPrice + (currentPrice * changeInPrice) / 100;
            st.setPrice(updatedPrice);
        }
        System.out.println("...price update complete...");
    }
}
