package com.bank.ib.nav.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "nav_snapshot",
        uniqueConstraints = @UniqueConstraint(columnNames = {"book", "valuation_date"}))
@Getter @Setter
public class NavSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String book;

    @Column(nullable = false)
    private LocalDate valuationDate;

    @Column(nullable = false)
    private BigDecimal cash;

    @Column(nullable = false)
    private BigDecimal marketValue;

    @Column(nullable = false)
    private BigDecimal nav;
}

