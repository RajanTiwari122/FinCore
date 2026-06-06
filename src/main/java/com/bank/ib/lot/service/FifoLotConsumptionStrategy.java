package com.bank.ib.lot.service;

import com.bank.ib.lot.entity.Lot;
import com.bank.ib.lot.model.LotConsumption;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class FifoLotConsumptionStrategy
        implements LotConsumptionStrategy {

    @Override
    public List<LotConsumption> consume(
            List<Lot> lots,
            BigDecimal sellQty) throws IllegalArgumentException {

        List<LotConsumption> result = new ArrayList<>();

        for (Lot lot : lots) {
            if (sellQty.signum() <= 0) break;

            BigDecimal usable =
                    lot.getRemainingQuantity().min(sellQty);

            result.add(new LotConsumption(lot, usable));
            sellQty = sellQty.subtract(usable);
        }

        if (sellQty.signum() > 0) {
            throw new IllegalStateException(
                    "Insufficient lots for SELL trade");
        }

        return result;
    }
}
