package com.bank.ib.pnl.service;

import com.bank.ib.lot.entity.Lot;
import com.bank.ib.lot.repository.LotRepository;
import com.bank.ib.pnl.entity.MarketPrice;
import com.bank.ib.pnl.entity.UnrealizedPnl;
import com.bank.ib.pnl.repository.MarketPriceRepository;
import com.bank.ib.pnl.repository.UnrealizedPnlRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UnrealizedPnlService {

    private final LotRepository lotRepo;
    private final MarketPriceRepository priceRepo;
    private final UnrealizedPnlRepository pnlRepo;

    public UnrealizedPnlService(
            LotRepository lotRepo,
            MarketPriceRepository priceRepo,
            UnrealizedPnlRepository pnlRepo) {
        this.lotRepo = lotRepo;
        this.priceRepo = priceRepo;
        this.pnlRepo = pnlRepo;
    }

    public void runValuation(LocalDate valuationDate) {

        // clear previous run (idempotent)
        pnlRepo.deleteByValuationDate(valuationDate);

        List<Lot> openLots =
                lotRepo.findByRemainingQuantityGreaterThan(BigDecimal.ZERO);

        for (Lot lot : openLots) {

            MarketPrice mp =
                    priceRepo.findByIsinAndPriceDate(
                                    lot.getIsin(), valuationDate)
                            .orElseThrow(() ->
                                    new IllegalStateException(
                                            "Missing market price for " + lot.getIsin()));

            BigDecimal pnl =
                    mp.getPrice()
                            .subtract(lot.getCostPrice())
                            .multiply(lot.getRemainingQuantity());

            UnrealizedPnl u = new UnrealizedPnl();
            u.setIsin(lot.getIsin());
            u.setBook(lot.getBook());
            u.setQuantity(lot.getRemainingQuantity());
            u.setCostPrice(lot.getCostPrice());
            u.setMarketPrice(mp.getPrice());
            u.setUnrealizedPnl(pnl);
            u.setValuationDate(valuationDate);

            pnlRepo.save(u);
        }
    }
}

