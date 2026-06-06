package com.bank.ib.performance.service;

import com.bank.ib.common.DayCountCalculator;
import com.bank.ib.fund.entity.FundNavSnapshot;
import com.bank.ib.fund.repository.FundNavSnapshotRepository;
import com.bank.ib.fund.repository.FundUnitRepository;
import com.bank.ib.performance.entity.HighWaterMark;
import com.bank.ib.performance.entity.PerformanceFeeAccrual;
import com.bank.ib.performance.entity.PerformanceFeeConfig;
import com.bank.ib.performance.repository.HighWaterMarkRepository;
import com.bank.ib.performance.repository.PerformanceFeeAccrualRepository;
import com.bank.ib.performance.repository.PerformanceFeeConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Transactional
public class PerformanceFeeService {

    private final PerformanceFeeConfigRepository configRepo;
    private final HighWaterMarkRepository hwmRepo;
    private final FundNavSnapshotRepository navRepo;
    private final DayCountCalculator dayCountCalculator;
    private final PerformanceFeeAccrualRepository accrualRepo;

    public PerformanceFeeService(PerformanceFeeConfigRepository configRepo,
                                 HighWaterMarkRepository hwmRepo,
                                 FundNavSnapshotRepository navRepo,
                                 DayCountCalculator dayCountCalculator,
                                 PerformanceFeeAccrualRepository accrualRepo) {
        this.configRepo = configRepo;
        this.hwmRepo = hwmRepo;
        this.navRepo = navRepo;
        this.dayCountCalculator = dayCountCalculator;
        this.accrualRepo = accrualRepo;
    }

    public void applyPerformanceFee(
            String fundId,
            LocalDate valuationDate) {

        PerformanceFeeConfig config =
                configRepo.findById(fundId)
                        .orElse(null);

        // ✅ No performance fee configured
        if (config == null) return;

        if(!dayCountCalculator.isFeePeriodEnd(config.getFrequency(), valuationDate))
            return;

        FundNavSnapshot nav =
                navRepo.findByFundIdAndValuationDate(fundId, valuationDate);

        BigDecimal navPerUnit =
                nav.getTotalNav()
                        .divide(nav.getTotalUnits(), 6, RoundingMode.HALF_UP);

        HighWaterMark hwm =
                hwmRepo.findById(fundId)
                        .orElseGet(() -> {
                            HighWaterMark h = new HighWaterMark();
                            h.setFundId(fundId);
                            h.setHwmNavPerUnit(navPerUnit);
                            h.setLastUpdated(valuationDate);
                            return h;
                        });

        // ❌ No new high
        if (navPerUnit.compareTo(hwm.getHwmNavPerUnit()) <= 0) {
            return;
        }

        // ✅ New profit above HWM
        BigDecimal excess =
                navPerUnit.subtract(hwm.getHwmNavPerUnit());

        BigDecimal feePerUnit =
                excess.multiply(config.getPerformanceFeeRate());

        BigDecimal totalFee =
                feePerUnit.multiply(nav.getTotalUnits());

        // Deduct from NAV
        nav.setTotalNav(nav.getTotalNav().subtract(totalFee));
        navRepo.save(nav);

        // Update HWM
        BigDecimal newNavPerUnit =
                nav.getTotalNav()
                        .divide(nav.getTotalUnits(), 6, RoundingMode.HALF_UP);

        hwm.setHwmNavPerUnit(newNavPerUnit);
        hwm.setLastUpdated(valuationDate);
        hwmRepo.save(hwm);

        // Audit record
        PerformanceFeeAccrual accrual = new PerformanceFeeAccrual();
        accrual.setFundId(fundId);
        accrual.setPeriodEnd(valuationDate);
        accrual.setNavPerUnitBeforeFee(navPerUnit);
        accrual.setHwm(hwm.getHwmNavPerUnit());
        accrual.setFeeAmount(totalFee);
        accrual.setNavPerUnitAfterFee(newNavPerUnit);

        accrualRepo.save(accrual);
    }
}
