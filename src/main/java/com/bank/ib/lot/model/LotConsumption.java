package com.bank.ib.lot.model;

import com.bank.ib.lot.entity.Lot;

import java.math.BigDecimal;

public class LotConsumption {

    private final Lot lot;
    private final BigDecimal quantityConsumed;

    public LotConsumption(Lot lot, BigDecimal quantityConsumed) {
        this.lot = lot;
        this.quantityConsumed = quantityConsumed;
    }

    public Lot getLot() {
        return lot;
    }

    public BigDecimal getQuantityConsumed() {
        return quantityConsumed;
    }
}

