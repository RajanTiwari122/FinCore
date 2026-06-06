package com.bank.ib.trading.economics;

import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.trading.entity.Trade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Component
public class BondTradeEconomics implements TradeEconomics {

    @Override
    public TradeEconomicsResult calculate(Trade trade,
                                          SecurityVersion version) {

        BigDecimal clean =
                trade.getQuantity()
                        .multiply(trade.getPrice())
                        .divide(BigDecimal.valueOf(100));

        BigDecimal accrued =
                calculateAccruedInterest(trade, version);

        TradeEconomicsResult result = new TradeEconomicsResult();
        result.setCleanAmount(clean);
        result.setAccruedInterest(accrued);
        result.setSettlementAmount(clean.add(accrued));

        return result;
    }

    private BigDecimal calculateAccruedInterest(
            Trade trade, SecurityVersion version) {

        // simplified ACT/365 example
        long days = ChronoUnit.DAYS.between(
                version.getEffectiveFrom(), trade.getTradeDate());

        return version.getCouponRate()
                .multiply(trade.getQuantity())
                .multiply(BigDecimal.valueOf(days))
                .divide(BigDecimal.valueOf(36500), RoundingMode.HALF_UP);
    }
}

