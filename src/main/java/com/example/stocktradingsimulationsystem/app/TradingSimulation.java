package com.example.stocktradingsimulationsystem.app;

import com.example.stocktradingsimulationsystem.stocktradingsystem.*;
import com.example.stocktradingsimulationsystem.service.*;
import com.example.stocktradingsimulationsystem.service.TradeProcessor;


import java.util.*;

/**
 * Main class to run the stock trading simulation.
 * Initializes stocks, generates trade requests, processes them concurrently,
 * and prints analytical reports with execution time.
 */
public class TradingSimulation {

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        // Step 1: Initialize stocks
        Map<StockSymbol, Stock> stockRegistry = new HashMap<>();
        stockRegistry.put(StockSymbol.AAPL, new Stock(StockSymbol.AAPL, 1000));
        stockRegistry.put(StockSymbol.GOOG, new Stock(StockSymbol.GOOG, 800));
        stockRegistry.put(StockSymbol.TSLA, new Stock(StockSymbol.TSLA, 1200));
        stockRegistry.put(StockSymbol.MSFT, new Stock(StockSymbol.MSFT, 900));
        stockRegistry.put(StockSymbol.AMZN, new Stock(StockSymbol.AMZN, 700));

        // Step 2: Generate random trade requests
        List<TradeRequest> tradeRequests = generateRandomTradeRequests(200, stockRegistry.keySet());

        // Step 3: Process trades concurrently
        TradeProcessor processor = new TradeProcessor(stockRegistry, 10); // 10 threads
        processor.processTrades(tradeRequests);

        // Step 4: Generate reports
        processor.generateReports();

        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Total execution time: " + durationMillis + " ms");
    }

    /**
     * Generates random trade requests for simulation.
     */
    private static List<TradeRequest> generateRandomTradeRequests(int count, Set<StockSymbol> stockSymbols) {
        List<TradeRequest> requests = new ArrayList<>();
        Random random = new Random();
        List<StockSymbol> symbolsList = new ArrayList<>(stockSymbols);

        for (int i = 0; i < count; i++) {
            String userId = "User" + (random.nextInt(10) + 1); // 10 users
            StockSymbol symbol = symbolsList.get(random.nextInt(symbolsList.size()));
            TradeType type = random.nextBoolean() ? TradeType.BUY : TradeType.SELL;
            int quantity = random.nextInt(50) + 1; // 1–50 units

            requests.add(new TradeRequest(userId, symbol, type, quantity));
        }
        return requests;
    }
}
