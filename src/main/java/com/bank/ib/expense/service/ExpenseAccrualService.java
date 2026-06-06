package com.bank.ib.expense.service;

import com.bank.ib.common.DayCountCalculator;
import com.bank.ib.expense.entity.ExpenseAccrual;
import com.bank.ib.expense.entity.FundExpenseConfig;
import com.bank.ib.expense.repository.ExpenseAccrualRepository;
import com.bank.ib.expense.repository.FundExpenseConfigRepository;
import com.bank.ib.fund.entity.FundNavSnapshot;
import com.bank.ib.fund.repository.FundNavSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ExpenseAccrualService {

    private final FundExpenseConfigRepository configRepo;
    private final FundNavSnapshotRepository navRepo;
    private final ExpenseAccrualRepository accrualRepo;
    private final DayCountCalculator dayCountCalculator;

    public ExpenseAccrualService(
            FundExpenseConfigRepository configRepo,
            FundNavSnapshotRepository navRepo,
            ExpenseAccrualRepository accrualRepo,
            DayCountCalculator dayCountCalculator) {
        this.configRepo = configRepo;
        this.navRepo = navRepo;
        this.accrualRepo = accrualRepo;
        this.dayCountCalculator = dayCountCalculator;
    }

    public void accrueDailyExpenses(String fundId, LocalDate date) {

        FundNavSnapshot navSnapshot =
                navRepo.findByFundIdAndValuationDate(fundId, date);

        BigDecimal nav = navSnapshot.getTotalNav();

        List<FundExpenseConfig> expenses =
                configRepo.findByFundId(fundId);

        if (expenses.isEmpty()) {
            return; // ✅ correct behavior
        }

        BigDecimal totalExpense = BigDecimal.ZERO;

        for (FundExpenseConfig exp : expenses) {

            BigDecimal yearFraction =
                    dayCountCalculator.yearFraction(
                            date.minusDays(1),
                            date,
                            exp.getDayCountConvention()
                    );

            BigDecimal expense =
                    nav.multiply(exp.getAnnualRate())
                            .multiply(yearFraction);

            totalExpense = totalExpense.add(expense);

            ExpenseAccrual accrual = new ExpenseAccrual();
            accrual.setFundId(fundId);
            accrual.setAccrualDate(date);
            accrual.setExpenseType(exp.getExpenseType());
            accrual.setExpenseAmount(expense);

            accrualRepo.save(accrual);
        }

        navSnapshot.setTotalNav(nav.subtract(totalExpense));
        navRepo.save(navSnapshot);
    }

}

