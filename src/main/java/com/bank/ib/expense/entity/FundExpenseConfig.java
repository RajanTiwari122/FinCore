package com.bank.ib.expense.entity;

import com.bank.ib.enums.DayCountConvention;
import com.bank.ib.enums.ExpenseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "fund_expense_config")
@Getter @Setter
public class FundExpenseConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundId;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    // annual rate e.g. 0.0075 = 0.75%
    private BigDecimal annualRate;

    @Enumerated(EnumType.STRING)
    private DayCountConvention dayCountConvention;
}


