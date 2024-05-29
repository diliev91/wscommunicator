package com.example.dto;

import java.math.BigDecimal;

public record OrderBookUpdate(String pair, BigDecimal price, Long amount, boolean isAsk) {}
