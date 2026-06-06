package com.bank.ib.cash.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cash_positions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"currency", "book"}))
@Getter @Setter
public class CashPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String book; // trader / desk (later fund)

    @Column(nullable = false)
    private BigDecimal balance;

    // getters & setters
}

