package com.bank.ib.lot.service;

import com.bank.ib.lot.entity.Lot;
import com.bank.ib.lot.model.LotConsumption;
import com.bank.ib.lot.repository.LotRepository;
import com.bank.ib.trading.entity.Trade;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class LotService {

    private final LotRepository repo;
    private final LotConsumptionStrategy strategy;

    public LotService(LotRepository repo,
                      FifoLotConsumptionStrategy strategy) {
        this.repo = repo;
        this.strategy = strategy;
    }

    // called on BUY
    public void createLot(Trade trade) {

        Lot lot = new Lot();
        lot.setIsin(trade.getIsin());
        lot.setBook(trade.getBookedBy());
        lot.setOriginalQuantity(trade.getQuantity());
        lot.setRemainingQuantity(trade.getQuantity());
        lot.setCostPrice(trade.getPrice());
        lot.setTradeDate(trade.getTradeDate());
        lot.setTradeId(trade.getTradeId());

        repo.save(lot);
    }

    // called on SELL
    public List<LotConsumption> consumeLots(Trade trade) {

        List<Lot> availableLots =
                repo.findByIsinAndBookAndRemainingQuantityGreaterThanOrderByTradeDateAsc(
                        trade.getIsin(),
                        trade.getBookedBy(),
                        BigDecimal.ZERO
                );

        List<LotConsumption> consumptions =
                strategy.consume(availableLots, trade.getQuantity());

        for (LotConsumption lc : consumptions) {
            Lot lot = lc.getLot();
            lot.setRemainingQuantity(
                    lot.getRemainingQuantity()
                            .subtract(lc.getQuantityConsumed()));
            repo.save(lot);
        }

        return consumptions;
    }
}

