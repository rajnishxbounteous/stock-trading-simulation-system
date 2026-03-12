package com.trading.domain;

/**
 * Represents a trade request made by a user.
 * Contains userId, stock symbol, trade type (BUY/SELL), and quantity.
 */
public class TradeRequest {

    private final String userId;
    private final StockSymbol stockSymbol;
    private final TradeType tradeType;
    private final int quantity;

    public TradeRequest(String userId, StockSymbol stockSymbol, TradeType tradeType, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Trade quantity must be positive");
        }
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.tradeType = tradeType;
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public StockSymbol getStockSymbol() {
        return stockSymbol;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "TradeRequest{" +
                "userId='" + userId + '\'' +
                ", stockSymbol=" + stockSymbol +
                ", tradeType=" + tradeType +
                ", quantity=" + quantity +
                '}';
    }
}
