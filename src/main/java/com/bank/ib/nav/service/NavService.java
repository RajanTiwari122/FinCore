package com.bank.ib.nav.service;

import com.bank.ib.cash.entity.CashPosition;
import com.bank.ib.cash.repository.CashPositionRepository;
import com.bank.ib.nav.entity.NavSnapshot;
import com.bank.ib.nav.repository.NavRepository;
import com.bank.ib.pnl.entity.UnrealizedPnl;
import com.bank.ib.pnl.repository.UnrealizedPnlRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class NavService {

    private final CashPositionRepository cashRepo;
    private final UnrealizedPnlRepository unrealizedRepo;
    private final NavRepository navRepo;

    public NavService(
            CashPositionRepository cashRepo,
            UnrealizedPnlRepository unrealizedRepo,
            NavRepository navRepo) {
        this.cashRepo = cashRepo;
        this.unrealizedRepo = unrealizedRepo;
        this.navRepo = navRepo;
    }

    public void calculateNav(String book, LocalDate valuationDate) {

        // idempotent
        navRepo.deleteByBookAndValuationDate(book, valuationDate);

        // 1️⃣ Cash
        BigDecimal cash =
                cashRepo.findByBook(book).stream()
                        .map(CashPosition::getBalance)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2️⃣ Market value of positions
        BigDecimal marketValue =
                unrealizedRepo.findByBookAndValuationDate(book, valuationDate)
                        .stream()
                        .map(e -> e.getMarketPrice().multiply(e.getQuantity()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3️⃣ NAV
        BigDecimal nav = cash.add(marketValue);

        NavSnapshot snap = new NavSnapshot();
        snap.setBook(book);
        snap.setValuationDate(valuationDate);
        snap.setCash(cash);
        snap.setMarketValue(marketValue);
        snap.setNav(nav);

        navRepo.save(snap);
    }
}

