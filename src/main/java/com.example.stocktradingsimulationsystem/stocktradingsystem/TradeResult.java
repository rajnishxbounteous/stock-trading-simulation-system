package com.example.stocktradingsimulationsystem.stocktradingsystem;

/**
 * Represents the result of processing a trade request.
 * Stores whether the trade was successful and how much quantity was executed.
 */
public class TradeResult {

    private final TradeRequest tradeRequest;
    private final boolean success;
    private final int executedQuantity;

    public TradeResult(TradeRequest tradeRequest, boolean success, int executedQuantity) {
        this.tradeRequest = tradeRequest;
        this.success = success;
        this.executedQuantity = executedQuantity;
    }

    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getExecutedQuantity() {
        return executedQuantity;
    }

    @Override
    public String toString() {
        return "TradeResult{" +
                "tradeRequest=" + tradeRequest +
                ", success=" + success +
                ", executedQuantity=" + executedQuantity +
                '}';
    }
}
