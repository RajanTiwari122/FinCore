package com.bank.ib.fund.service;

import com.bank.ib.fund.entity.FundNavSnapshot;
import com.bank.ib.fund.entity.FundUnitBalance;
import com.bank.ib.fund.repository.FundNavSnapshotRepository;
import com.bank.ib.fund.repository.FundUnitRepository;
import com.bank.ib.nav.repository.NavRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Transactional
public class FundNavService {

    private final NavRepository navRepo;
    private final FundUnitRepository unitRepo;
    private final FundNavSnapshotRepository fundNavRepo;

    public FundNavService(
            NavRepository navRepo,
            FundUnitRepository unitRepo,
            FundNavSnapshotRepository fundNavRepo) {
        this.navRepo = navRepo;
        this.unitRepo = unitRepo;
        this.fundNavRepo = fundNavRepo;
    }

    public void calculateFundNav(
            String fundId,
            String book,
            LocalDate valuationDate) {

        // 1️⃣ Book-level NAV
        BigDecimal totalNav =
                navRepo.findByBookAndValuationDate(book, valuationDate)
                        .getNav();

        // 2️⃣ Total units
        FundUnitBalance units =
                unitRepo.findById(fundId)
                        .orElseThrow(() ->
                                new IllegalStateException("Units not initialized"));

        BigDecimal totalUnits = units.getTotalUnits();

        if (totalUnits.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("No units outstanding");
        }

        // 3️⃣ NAV per unit
        BigDecimal navPerUnit =
                totalNav.divide(
                        totalUnits,
                        6,
                        RoundingMode.HALF_UP
                );

        // 4️⃣ Persist snapshot
        FundNavSnapshot snap = new FundNavSnapshot();
        snap.setFundId(fundId);
        snap.setValuationDate(valuationDate);
        snap.setTotalNav(totalNav);
        snap.setTotalUnits(totalUnits);
        snap.setNavPerUnit(navPerUnit);

        fundNavRepo.save(snap);
    }
}

