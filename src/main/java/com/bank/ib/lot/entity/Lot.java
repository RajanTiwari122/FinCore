package com.bank.ib.lot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lots")
@Getter @Setter
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String isin;

    @Column(nullable = false)
    private String book;

    @Column(nullable = false)
    private BigDecimal originalQuantity;

    @Column(nullable = false)
    private BigDecimal remainingQuantity;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private LocalDate tradeDate;

    @Column(nullable = false)
    private String tradeId;

    // getters only for immutable fields
}

