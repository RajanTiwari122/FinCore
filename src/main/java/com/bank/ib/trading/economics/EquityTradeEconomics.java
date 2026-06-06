package com.bank.ib.trading.economics;

import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.trading.entity.Trade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EquityTradeEconomics implements TradeEconomics {

    @Override
    public TradeEconomicsResult calculate(Trade trade,
                                          SecurityVersion version) {

        BigDecimal clean =
                trade.getQuantity().multiply(trade.getPrice());

        TradeEconomicsResult result = new TradeEconomicsResult();
        result.setCleanAmount(clean);
        result.setAccruedInterest(BigDecimal.ZERO);
        result.setSettlementAmount(clean);

        return result;
    }
}

