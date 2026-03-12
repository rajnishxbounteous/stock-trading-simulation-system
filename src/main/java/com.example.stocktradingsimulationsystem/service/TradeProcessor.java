package com.example.stocktradingsimulationsystem.service;

//import com.example.stocktradingsimulationsystem;
import com.example.stocktradingsimulationsystem.stocktradingsystem.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for processing trade requests concurrently.
 * Uses ExecutorService to handle multiple user trades in parallel.
 * Generates optimized reports using parallel streams.
 */
public class TradeProcessor {

    private final Map<StockSymbol, Stock> stockRegistry;
    private final ExecutorService executorService;
    private final List<TradeResult> tradeResults;

    public TradeProcessor(Map<StockSymbol, Stock> stockRegistry, int threadPoolSize) {
        this.stockRegistry = stockRegistry;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.tradeResults = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Submits all trade requests for concurrent processing.
     */
    public void processTrades(List<TradeRequest> tradeRequests) {
        List<Future<TradeResult>> futures = new ArrayList<>();

        for (TradeRequest request : tradeRequests) {
            futures.add(executorService.submit(() -> executeTrade(request)));
        }

        // Wait for all tasks to complete and collect results
        for (Future<TradeResult> future : futures) {
            try {
                TradeResult result = future.get();
                if (result.isSuccess()) {
                    tradeResults.add(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error processing trade: " + e.getMessage());
                Thread.currentThread().interrupt(); // restore interrupt status
            }
        }

        shutdownExecutor();
    }

    /**
     * Executes a single trade request safely.
     */
    private TradeResult executeTrade(TradeRequest request) {
        Stock stock = stockRegistry.get(request.getStockSymbol());
        boolean success = false;
        int executedQuantity = 0;

        if (stock != null) {
            synchronized (stock) {
                if (request.getTradeType() == TradeType.BUY) {
                    success = stock.buyStock(request.getQuantity());
                    executedQuantity = success ? request.getQuantity() : 0;
                } else if (request.getTradeType() == TradeType.SELL) {
                    stock.sellStock(request.getQuantity());
                    success = true;
                    executedQuantity = request.getQuantity();
                }
            }
        }

        return new TradeResult(request, success, executedQuantity);
    }

    /**
     * Generates analytical reports using parallel streams.
     * Handles edge cases gracefully (e.g., no trades).
     */
    public void generateReports() {
        System.out.println("=== Trade Reports ===");

        if (tradeResults.isEmpty()) {
            System.out.println("No successful trades to report.");
            return;
        }

        // Total successful trades
        long totalTrades = tradeResults.size();
        System.out.println("Total successful trades: " + totalTrades);

        // Quantity traded per stock (parallel stream + concurrent collector)
        Map<StockSymbol, Integer> quantityPerStock = tradeResults.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        result -> result.getTradeRequest().getStockSymbol(),
                        Collectors.summingInt(TradeResult::getExecutedQuantity)
                ));
        System.out.println("Quantity traded per stock: " + quantityPerStock);

        // Most traded stock(s)
        quantityPerStock.entrySet().parallelStream()
                .max(Map.Entry.comparingByValue())
                .ifPresentOrElse(
                        entry -> System.out.println("Most traded stock: " + entry.getKey() + " (" + entry.getValue() + ")"),
                        () -> System.out.println("No trades recorded for any stock.")
                );

        // Trades per user (parallel stream + concurrent collector)
        Map<String, Long> tradesPerUser = tradeResults.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        result -> result.getTradeRequest().getUserId(),
                        Collectors.counting()
                ));
        System.out.println("Trades per user: " + tradesPerUser);
    }

    /**
     * Shuts down the executor service gracefully.
     */
    private void shutdownExecutor() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
