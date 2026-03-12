package com.trading.domain;

/**
 * Enum representing the type of trade request.
 * BUY decreases available stock quantity, SELL increases it.
 */
public enum TradeType {
    BUY,
    SELL
}
