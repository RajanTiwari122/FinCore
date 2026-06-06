package com.bank.ib.performance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "performance_fee_accrual",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"fund_id", "period_end"}
        )
)
@Getter @Setter
public class PerformanceFeeAccrual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundId;
    private LocalDate periodEnd;

    private BigDecimal navPerUnitBeforeFee;
    private BigDecimal hwm;
    private BigDecimal feeAmount;
    private BigDecimal navPerUnitAfterFee;
}

