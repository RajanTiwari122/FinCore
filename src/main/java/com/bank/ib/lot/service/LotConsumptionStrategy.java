package com.bank.ib.lot.service;

import com.bank.ib.lot.entity.Lot;
import com.bank.ib.lot.model.LotConsumption;

import java.math.BigDecimal;
import java.util.List;

public interface LotConsumptionStrategy {

    List<LotConsumption> consume(
            List<Lot> availableLots,
            BigDecimal sellQuantity
    );
}
