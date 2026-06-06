package com.bank.ib.reference.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "bond_accrued_interest",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"isin", "accrual_date"}
        )
)
@Getter @Setter
public class BondAccruedInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isin;
    private LocalDate accrualDate;

    private BigDecimal accruedInterest;
}

