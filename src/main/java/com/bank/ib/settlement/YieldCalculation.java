package com.bank.ib.settlement;

import org.quantlib.*;
import org.springframework.stereotype.Service;

@Service
public class YieldCalculation {
    public double yieldCalculation(){
        double yield = 0;

        Date settleDate = new Date(1, Month.September,2049);
        Date maturityDate = new Date(31, Month.January,2050);
        Date startDate = new Date(1, Month.January,2047);

        Settings.instance().setEvaluationDate(settleDate);
        DayCounter daycounter = new ActualActual(ActualActual.Convention.Bond);

        Schedule schedule = new Schedule(startDate,maturityDate,new Period(Frequency.Annual),new NullCalendar(),BusinessDayConvention.Unadjusted,BusinessDayConvention.Unadjusted, DateGeneration.Rule.Backward,true);

        DoubleVector coupon = new DoubleVector();
        coupon.add(0.085);

        FixedRateBond bond = new FixedRateBond(0,100,schedule,coupon,daycounter);

        double cleanPrice = 99.5;

        yield = bond.yield(cleanPrice,daycounter,Compounding.Compounded,Frequency.Annual);

        return yield;
    }

    public static void main(String[] args) {
        System.out.printf("Yield = %.16 f%%\n",new YieldCalculation().yieldCalculation()*100);

    }
}
