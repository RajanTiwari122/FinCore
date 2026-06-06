package com.bank.ib.marketdata.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MarketPriceResponse {

    public String isin;
    public String yahooSymbol;
    public LocalDate priceDate;
    public BigDecimal price;

    public MarketPriceResponse(
            String isin,
            String yahooSymbol,
            LocalDate priceDate,
            BigDecimal price) {
        this.isin = isin;
        this.yahooSymbol = yahooSymbol;
        this.priceDate = priceDate;
        this.price = price;
    }
}

