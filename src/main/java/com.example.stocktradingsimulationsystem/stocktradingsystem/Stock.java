package com.trading.domain;

/**
 * Represents a stock in the trading system.
 * Each stock has a symbol (from StockSymbol enum) and an available quantity.
 * Quantity updates are synchronized to ensure thread safety when accessed concurrently.
 */
public class Stock {

    private final StockSymbol stockSymbol;
    private int availableQuantity;

    public Stock(StockSymbol stockSymbol, int availableQuantity) {
        this.stockSymbol = stockSymbol;
        this.availableQuantity = availableQuantity;
    }

    public StockSymbol getStockSymbol() {
        return stockSymbol;
    }

    public synchronized int getAvailableQuantity() {
        return availableQuantity;
    }

    /**
     * Attempts to buy the given quantity of stock.
     * Returns true if successful, false if insufficient quantity.
     */
    public synchronized boolean buyStock(int quantity) {
        if (availableQuantity >= quantity) {
            availableQuantity -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Sells the given quantity of stock.
     * Always succeeds since selling increases available quantity.
     */
    public synchronized void sellStock(int quantity) {
        availableQuantity += quantity;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol=" + stockSymbol +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
}
