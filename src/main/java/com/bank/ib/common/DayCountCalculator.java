package com.bank.ib.common;

import com.bank.ib.enums.DayCountConvention;
import com.bank.ib.enums.PerformanceFeeFrequency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Component
public class DayCountCalculator {

    public BigDecimal yearFraction(
            LocalDate start,
            LocalDate end,
            DayCountConvention convention) {

        long days = ChronoUnit.DAYS.between(start, end);

        return switch (convention) {

            case ACT_365 ->
                    BigDecimal.valueOf(days)
                            .divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);

            case ACT_360 ->
                    BigDecimal.valueOf(days)
                            .divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_UP);

            case ACT_ACT -> {
                int year = start.getYear();
                int daysInYear =
                        Year.isLeap(year) ? 366 : 365;

                yield BigDecimal.valueOf(days)
                        .divide(BigDecimal.valueOf(daysInYear), 10, RoundingMode.HALF_UP);
            }
        };
    }

    public boolean isFeePeriodEnd(
            PerformanceFeeFrequency freq,
            LocalDate date) {

        return switch (freq) {
            case MONTHLY -> date.equals(date.with(TemporalAdjusters.lastDayOfMonth()));
            case QUARTERLY -> Month.of(date.getMonthValue()).firstMonthOfQuarter()
                    .minus(2)
                    == date.getMonth();
            case ANNUAL -> date.getMonth() == Month.DECEMBER
                    && date.getDayOfMonth() == 31;
        };
    }

}

