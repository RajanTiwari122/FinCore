package com.bank.ib.fund.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "fund_nav_snapshot",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fund_id", "valuation_date"})
)
@Getter
@Setter
public class FundNavSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundId; // fundCode & fundId are same
    private LocalDate valuationDate;

    private BigDecimal totalNav;
    private BigDecimal totalUnits;
    private BigDecimal navPerUnit;
}

