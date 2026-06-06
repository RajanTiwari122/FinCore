package com.bank.ib.trading.economics;

import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.trading.entity.Trade;

public interface TradeEconomics {

    TradeEconomicsResult calculate(Trade trade, SecurityVersion version);
}
