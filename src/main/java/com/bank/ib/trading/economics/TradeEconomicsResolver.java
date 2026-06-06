package com.bank.ib.trading.economics;

import com.bank.ib.enums.SecurityType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class TradeEconomicsResolver {

    private final Map<SecurityType, TradeEconomics> strategies;

    public TradeEconomicsResolver(
            EquityTradeEconomics equityEconomics,
            BondTradeEconomics bondEconomics) {

        this.strategies = new EnumMap<>(SecurityType.class);
        strategies.put(SecurityType.Equity, equityEconomics);
        strategies.put(SecurityType.ETF, equityEconomics);
        strategies.put(SecurityType.Bond, bondEconomics);
    }

    public TradeEconomics resolve(SecurityType type) {
        TradeEconomics economics = strategies.get(type);
        if (economics == null) {
            throw new IllegalStateException(
                    "No trade economics defined for security type: " + type);
        }
        return economics;
    }
}

