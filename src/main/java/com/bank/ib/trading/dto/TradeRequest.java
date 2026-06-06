package com.bank.ib.trading.dto;

import com.bank.ib.enums.TradeSide;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeRequest {

    public String isin;
    public LocalDate tradeDate;
    public BigDecimal quantity;
    public BigDecimal price;
    public TradeSide side;   // BUY / SELL
}
