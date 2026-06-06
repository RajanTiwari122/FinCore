package com.bank.ib.trading.economics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class TradeEconomicsResult {

    private BigDecimal cleanAmount;
    private BigDecimal accruedInterest;
    private BigDecimal settlementAmount;

    // getters & setters
}

