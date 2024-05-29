package com.example.manager;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;



public class OrderBook {
    private final TreeMap<BigDecimal, Long> asks = new TreeMap<>();
    private final TreeMap<BigDecimal, Long> bids = new TreeMap<>(Comparator.reverseOrder());

    public void updateAsk(BigDecimal price, Long amount) {
        updateMap(price, amount, asks);
    }

    public void updateBid(BigDecimal price, Long amount) {
        updateMap(price, amount, bids);
    }

    public Map.Entry<BigDecimal, Long> getBestBid(){
        return bids.firstEntry();
    }
    public Map.Entry<BigDecimal, Long> getBestAsk() {
        return asks.firstEntry();
    }
    private void updateMap(BigDecimal price, Long amount, TreeMap<BigDecimal, Long> map) {
        map.remove(price);
        if (amount > 0) {
            map.put(price, amount);
        }
    }

    @Override
    public String toString() {
        StringBuilderPlus sb = new StringBuilderPlus();
        sb.appendLine("<------------------------------------>");
        sb.appendLine("asks:");
        asks.reversed().forEach((price, amount) -> sb.appendLine(String.format("[ %s, %s ]", price, amount)));
        Map.Entry<BigDecimal, Long> bestBid = getBestBid();
        sb.appendLine(String.format("best bid: [ %s ]", bestBid == null ? "" : String.format("%s, %s", bestBid.getKey(), bestBid.getValue())));
        Map.Entry<BigDecimal, Long> bestAsk = getBestAsk();
        sb.appendLine(String.format("best ask: [ %s ]", bestAsk == null ? "" : String.format("%s, %s", bestAsk.getKey(), bestAsk.getValue())));
        sb.appendLine("bids:");
        bids.forEach((price, amount) -> sb.appendLine(String.format("[ %s, %s ]", price, amount)));
        sb.appendLine(">------------------------------------<");
        return sb.toString();
    }
}
