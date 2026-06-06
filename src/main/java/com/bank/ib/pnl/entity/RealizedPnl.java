package com.bank.ib.pnl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "realized_pnl")
@Getter @Setter
public class RealizedPnl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SELL trade (internal ID)
    @Column(nullable = false)
    private String sellTradeId;

    // BUY trade (internal ID from lot)
    @Column(nullable = false)
    private String buyTradeId;

    @Column(nullable = false)
    private String isin;

    @Column(nullable = false)
    private String book;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal buyPrice;

    @Column(nullable = false)
    private BigDecimal sellPrice;

    @Column(nullable = false)
    private BigDecimal realizedPnl;

    @Column(nullable = false)
    private LocalDate tradeDate;

    // getters & setters
}

