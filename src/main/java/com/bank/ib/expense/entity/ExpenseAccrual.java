package com.bank.ib.expense.entity;

import com.bank.ib.enums.ExpenseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "expense_accrual",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"fund_id", "accrual_date"}
        )
)
@Getter @Setter
public class ExpenseAccrual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundId;
    private LocalDate accrualDate;
    private ExpenseType expenseType;

    private BigDecimal navBeforeExpense;
    private BigDecimal expenseAmount;
    private BigDecimal navAfterExpense;
}

