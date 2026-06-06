package com.bank.ib.pnl.service;

import com.bank.ib.lot.entity.Lot;
import com.bank.ib.lot.model.LotConsumption;
import com.bank.ib.pnl.entity.RealizedPnl;
import com.bank.ib.pnl.repository.RealizedPnlRepository;
import com.bank.ib.trading.entity.Trade;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class RealizedPnlService {

    private final RealizedPnlRepository repo;

    public RealizedPnlService(RealizedPnlRepository repo) {
        this.repo = repo;
    }

    public void bookRealizedPnl(
            Trade sellTrade,
            List<LotConsumption> consumptions) {

        for (LotConsumption lc : consumptions) {

            Lot lot = lc.getLot();
            BigDecimal qty = lc.getQuantityConsumed();

            BigDecimal pnl =
                    sellTrade.getPrice()
                            .subtract(lot.getCostPrice())
                            .multiply(qty);

            RealizedPnl rp = new RealizedPnl();
            rp.setSellTradeId(sellTrade.getTradeId());
            rp.setBuyTradeId(lot.getTradeId());
            rp.setIsin(sellTrade.getIsin());
            rp.setBook(sellTrade.getBookedBy());
            rp.setQuantity(qty);
            rp.setBuyPrice(lot.getCostPrice());
            rp.setSellPrice(sellTrade.getPrice());
            rp.setRealizedPnl(pnl);
            rp.setTradeDate(sellTrade.getTradeDate());

            repo.save(rp);
        }
    }
}

