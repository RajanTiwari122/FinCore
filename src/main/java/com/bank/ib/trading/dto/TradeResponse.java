package com.bank.ib.trading.dto;

import com.bank.ib.enums.TradeSide;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class
TradeResponse {
    public Long id;
    public String tradeId;
    public String isin;
    public LocalDate tradeDate;
    public BigDecimal quantity;
    public BigDecimal price;

    public BigDecimal cleanAmount;
    public BigDecimal accruedInterest;
    public BigDecimal settlementAmount;

    public String bookedBy;
    public Instant bookedAt;
    public TradeSide side;
}

