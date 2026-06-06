package com.bank.ib.reference.service;

import com.bank.ib.common.DayCountCalculator;
import com.bank.ib.reference.entity.BondAccruedInterest;
import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.reference.repository.BondAccruedInterestRepository;
import com.bank.ib.reference.repository.BondCouponScheduleRepository;
import com.bank.ib.reference.repository.SecurityVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class BondCouponAccrualService {

    private final SecurityVersionRepository versionRepo;
    private final BondCouponScheduleRepository scheduleRepo;
    private final BondAccruedInterestRepository accrualRepo;
    private final DayCountCalculator dayCountCalculator;

    public BondCouponAccrualService(SecurityVersionRepository versionRepo,
                                    BondCouponScheduleRepository scheduleRepo,
                                    BondAccruedInterestRepository accrualRepo,
                                    DayCountCalculator dayCountCalculator) {
        this.versionRepo = versionRepo;
        this.scheduleRepo = scheduleRepo;
        this.accrualRepo = accrualRepo;
        this.dayCountCalculator = dayCountCalculator;
    }

    public void accrueCoupon(
            String isin,
            LocalDate valuationDate) {

        SecurityVersion bond =
                versionRepo.findBondVersionAsOf(isin, valuationDate)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "No bond version found as of " + valuationDate));

        LocalDate lastCouponDate =
                scheduleRepo.findLastCouponDate(isin, valuationDate);

        BigDecimal yearFraction =
                dayCountCalculator.yearFraction(
                        lastCouponDate,
                        valuationDate,
                        bond.getDayCountConvention()
                );

        BigDecimal annualCoupon =
                bond.getFaceValue()
                        .multiply(bond.getCouponRate());

        BigDecimal accruedInterest =
                annualCoupon.multiply(yearFraction);

        BondAccruedInterest accrual = new BondAccruedInterest();
        accrual.setIsin(isin);
        accrual.setAccrualDate(valuationDate);
        accrual.setAccruedInterest(accruedInterest);

        accrualRepo.save(accrual);
    }
}

